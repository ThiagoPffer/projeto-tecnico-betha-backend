package com.thiagobetha.projeto_tecnico.services;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.thiagobetha.projeto_tecnico.domain.OrdemServico;

public abstract class AbstractEmailService implements EmailService{

	@Value("${default.sender}")
	private String sender;
	
	@Override
	public void sendOrderConfirmationEmail(OrdemServico obj) {
		SimpleMailMessage sm = prepareOrderConfirmationEmail(obj);
		sendEmail(sm);
	}
	
	@Override
	public void sendOrderConclusionEmail(OrdemServico obj) {
		SimpleMailMessage sm = prepareOrderConclusionEmail(obj);
		sendEmail(sm);
	}
	
	@Override
	public void sendCancellationConfirmationEmail(OrdemServico obj) {
		SimpleMailMessage sm = prepareCancellationConfirmationEmail(obj);
		sendEmail(sm);
	}
	
	protected SimpleMailMessage prepareOrderConfirmationEmail(OrdemServico obj) {
		NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(obj.getCliente().getEmail());
		sm.setFrom(sender);
		sm.setSubject("Análise da ordem de serviço finalizada! Número de ordem: " +obj.getId());
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText(
				"Olá " +obj.getCliente().getNome()+"!"+
				"\nA análise da sua ordem de serviço pelo técnico foi concluída!"+
				"\nValor total a pagar: " +nf.format(obj.getValorTotal())+
				"\nEscolha abaixo se deseja dar continuidade na manutenção ou cancelar a ordem: \n"+
				"http://localhost:8080/ordensservico/"+obj.getId()+"/situacoes?value=CONCLUIDA \n"+
				"http://localhost:8080/ordensservico/"+obj.getId()+"/situacoes?value=CANCELADA"+
				"\nDados da ordem: \n"+
				obj.toString()
				);
		return sm;
	}
	
	protected SimpleMailMessage prepareOrderConclusionEmail(OrdemServico obj) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(obj.getCliente().getEmail());
		sm.setFrom(sender);
		sm.setSubject("Manutenção concluída! Número de ordem: " +obj.getId());
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText(
				"Olá " +obj.getCliente().getNome()+"!"+
				"\nSua ordem de serviço foi concluída e seus itens estão disponíveis para retirada!"+
				"\nDados da ordem: \n"+
				obj.toString()
				);
		return sm;
	}
	
	protected SimpleMailMessage prepareCancellationConfirmationEmail(OrdemServico obj) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(obj.getCliente().getEmail());
		sm.setFrom(sender);
		sm.setSubject("Cancelamento de ordem de serviço confirmado! Número de ordem: " +obj.getId());
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText(
				"Olá " +obj.getCliente().getNome()+"!"+
				"\nSua solicitação de cancelamento da ordem de serviço foi confirmada e seus itens estão disponíveis para retirada!"+
				"\nDados da ordem: \n"+
				obj.toString()
				);
		return sm;
	}
	
}
