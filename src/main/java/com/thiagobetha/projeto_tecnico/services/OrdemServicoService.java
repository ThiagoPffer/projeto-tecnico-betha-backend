package com.thiagobetha.projeto_tecnico.services;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.thiagobetha.projeto_tecnico.domain.Cliente;
import com.thiagobetha.projeto_tecnico.domain.ItemImagem;
import com.thiagobetha.projeto_tecnico.domain.ItemOrdemServico;
import com.thiagobetha.projeto_tecnico.domain.OrdemServico;
import com.thiagobetha.projeto_tecnico.domain.enums.EstadoPagamento;
import com.thiagobetha.projeto_tecnico.domain.enums.SituacaoOrdemServico;
import com.thiagobetha.projeto_tecnico.dto.OrdemServicoDTO;
import com.thiagobetha.projeto_tecnico.repositories.ItemImagensRepository;
import com.thiagobetha.projeto_tecnico.repositories.ItensRepository;
import com.thiagobetha.projeto_tecnico.repositories.OrdemServicoRepository;
import com.thiagobetha.projeto_tecnico.services.exceptions.DataIntegrityException;
import com.thiagobetha.projeto_tecnico.services.exceptions.InvalidSituationException;
import com.thiagobetha.projeto_tecnico.services.exceptions.ObjectNotFoundException;

@Service
public class OrdemServicoService {
	
	@Autowired
	private OrdemServicoRepository repo;
	
	@Autowired
	private ItensRepository itensRepo;
	
	@Autowired
	private ItemImagensRepository itemImagensRepo;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private ImageService imageService;
	
	@Value("${img.prefix.item.ordemservico.1}")
	private String prefix1;
	
	@Value("${img.prefix.item.ordemservico.2}")
	private String prefix2;
	
	@Value("${img.default.size}")
	private Integer size;
	
	public List<OrdemServico> findAll(){
		List<OrdemServico> list = repo.findAll();
		if(list.isEmpty()) {
			throw new ObjectNotFoundException("Nenhuma ordem de serviço foi encontrada!");
		}
		return list;
	}
	
	public Page<OrdemServico> findPage(Integer page, Integer linesPerPage, String direction, String orderBy){
		PageRequest pageReq = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		if(repo.findAll(pageReq).getContent().isEmpty()) {
			throw new ObjectNotFoundException("Nenhuma ordem de serviço foi encontrada!");
		}
		return repo.findAll(pageReq);
	}
	
	public OrdemServico findOne(Integer id) {
		Optional<OrdemServico> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Ordem de serviço de id " + id + " não encontrada!"));
	}
	
	@Transactional
	public OrdemServico insert(OrdemServico obj) {
		obj.setId(null);
		atualizarValorTotal(obj);
		repo.save(obj);
		return obj;
	}
	
	/*
	 * //REVER COM O MICHEL COM RELAÇÃO AO ITENS_ORDEM_SERVICO_REPOSITORY
	 * Uma provável solução para este problema sem usar o ItensOrdemServicoRepository seria fazer uma query
	 * no repositorio da ordemservico que delete os itens desejados da tabela dos itens.
	 * "DELETE FROM ITEM_ORDEM_SERVICO WHERE ORDEM_SERVICO_ID = <id_da_ordem>"
	*/
	@Transactional 
	public OrdemServico update(OrdemServico newObj) {
		OrdemServico obj = findOne(newObj.getId());
		
		if(!obj.getSituacao().equals(SituacaoOrdemServico.EM_ANALISE)) {
			throw new DataIntegrityException("Não é possível alterar dados de uma ordem na seguinte situação: " + obj.getSituacao());
		}
		
		obj.getItens().forEach(item -> {
			if (!newObj.getItens().contains(item)) {
				itensRepo.deleteById(item.getId());
			}
		});
		
		atualizarValorTotal(newObj);
		return repo.save(newObj);
	}
	
	@Transactional
	public OrdemServico updateSituacao(SituacaoOrdemServico situacao, Integer id){
		OrdemServico obj = findOne(id);
		verificaSituacao(situacao, obj.getSituacao());
		obj.setSituacao(situacao);
		
		if(obj.getSituacao().equals(SituacaoOrdemServico.CANCELADA)) {
			updateEstadoPagamento(EstadoPagamento.CANCELADO, id);
		}
		
		sendEmail(obj);
		return repo.save(obj);
	}
	
	@Transactional
	public OrdemServico updateEstadoPagamento(EstadoPagamento estado, Integer id){
		OrdemServico obj = findOne(id);
		obj.setPagamento(estado);
		return repo.save(obj);
	}

	public void delete(Integer id) {
		findOne(id);
		repo.deleteById(id);
	}
	
	public OrdemServico fromDTO(OrdemServicoDTO newObj) {
		Cliente cliente = clienteService.findOne(newObj.getIdCliente());
		OrdemServico os = new OrdemServico(cliente);
		os.setItens(newObj.getItens());
		
		for(ItemOrdemServico i : newObj.getItens()) {
			i.setOrdemServico(os);
		}
		
		return os;
	}
	
	private void atualizarValorTotal(OrdemServico obj) {
		List<ItemOrdemServico> list = obj.getItens();
		obj.setValorTotal(BigDecimal.ZERO);
		for(ItemOrdemServico item : list) {
			obj.setValorTotal(item.getOrcamento());
		}
	}
	
	private void verificaSituacao(SituacaoOrdemServico newSituacao, SituacaoOrdemServico situacao) {
		
		//Checa se a nova situacao é válida, comparando com a situação atual.
		switch(situacao.getCod()) {
			case 1: //EM_ANALISE
				if(newSituacao.equals(SituacaoOrdemServico.APROVADA) 
						|| newSituacao.equals(SituacaoOrdemServico.CONCLUIDA)) {
					throw new InvalidSituationException("Mudança inválida de situação: " + situacao + " para " + newSituacao);
				}
				break;
			case 2: //AGUARDANDO_DECISAO
				if(newSituacao.equals(SituacaoOrdemServico.CONCLUIDA)) {
					throw new InvalidSituationException("Mudança inválida de situação: " + situacao + " para " + newSituacao);
				}
				break;
			case 3: //APROVADA
				if(!newSituacao.equals(SituacaoOrdemServico.CONCLUIDA)) {
					throw new InvalidSituationException("Mudança inválida de situação: " + situacao + " para " + newSituacao);
				}
				break;
			case 4: //CONCLUIDA
				throw new InvalidSituationException("Mudança inválida de situação: " + situacao + " para " + newSituacao);
			case 5: //CANCELADA
				throw new InvalidSituationException("Mudança inválida de situação: " + situacao + " para " + newSituacao);
		}

	}
	
	private void sendEmail(OrdemServico obj) {
		
		switch(obj.getSituacao().getCod()) {
			case 2: 
				emailService.sendOrderConfirmationEmail(obj);
				break;
			case 4:
				emailService.sendOrderConclusionEmail(obj);
				break;
			case 5:
				emailService.sendCancellationConfirmationEmail(obj);
				break;
		}
	}
	
	public ItemImagem uploadItensPictures(Integer id, Integer idItem, MultipartFile multipartFile) {
		ItemOrdemServico item = itensRepo.findById(idItem)
				.orElseThrow(() -> new ObjectNotFoundException("Item de id "+idItem+" não encontrado!"));

		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		jpgImage = imageService.cropSquare(jpgImage);
		jpgImage = imageService.resize(jpgImage, size);
		String fileName = prefix1 + idItem + prefix2 + item.getImagens().size() + ".jpg";
		
		s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");

		ItemImagem itemImg = new ItemImagem(item, fileName);
		item.addImagens(itemImg);
		Integer indexImage = item.getImagens().indexOf(itemImg);
		
		itemImg = itensRepo.save(item).getImagens().get(indexImage);
		
		return itemImg;
	}
	
	public void deleteItensPictures(Integer idImagem) {
		ItemImagem itemImg = itemImagensRepo.findById(idImagem)
				.orElseThrow(() -> new ObjectNotFoundException("Imagem de id "+idImagem+" não encontrada!"));
		
		s3Service.deleteFile(itemImg.getNomeArquivo());
		itemImagensRepo.deleteById(idImagem);
	}
	
}
