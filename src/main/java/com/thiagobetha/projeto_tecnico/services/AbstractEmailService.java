package com.thiagobetha.projeto_tecnico.services;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.thiagobetha.projeto_tecnico.domain.Funcionario;
import com.thiagobetha.projeto_tecnico.domain.OrdemServico;

public abstract class AbstractEmailService implements EmailService{

	@Value("${default.sender}")
	private String sender;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
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
	
	@Override
	public void sendPasswordRequestEmail(Funcionario funcionario, String newPass) {
		SimpleMailMessage sm = preparePasswordRequestEmail(funcionario, newPass);
		sendEmail(sm);
	}
	
	@Override
	public void sendNewPasswordEmail(Funcionario funcionario, String newPass) {
		SimpleMailMessage sm = prepareNewPasswordEmail(funcionario, newPass);
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
				"http://localhost:8080/ordensservico/"+obj.getId()+"/situacoes?value=APROVADA \n"+
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
	
	protected SimpleMailMessage preparePasswordRequestEmail(Funcionario funcionario, String token) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(funcionario.getEmail());
		sm.setFrom(sender);
		sm.setSubject("Solicitação de nova senha");
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText("Clique para mudar a senha: \n" 
		+ "http://localhost:8080/auth/newpassword?token=" + token);
		return sm;
	}
	
	protected SimpleMailMessage prepareNewPasswordEmail(Funcionario funcionario, String newPass) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(funcionario.getEmail());
		sm.setFrom(sender);
		sm.setSubject("Solicitação de nova senha");
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText("Nova senha: " + newPass);
		return sm;
	}
	
	// EMAIL HTML:
	
	protected String htmlFromTemplateOrdemServico(OrdemServico obj, String token) {
		Context context = new Context();
		context.setVariable("ordemServico", obj);
		context.setVariable("token", token);
		return templateEngine.process("email/confirmacaoOrdemServico", context);
	}
	
	@Override
	public void sendOrderConfirmationHtmlEmail(OrdemServico obj, String token) {
		try {
			MimeMessage mm = prepareMimeMessageOrderConfirmationEmail(obj, token);
			sendHtmlEmail(mm);
		} catch (MessagingException e) {
			sendOrderConfirmationEmail(obj);
		}
	}

	protected MimeMessage prepareMimeMessageOrderConfirmationEmail(OrdemServico obj, String token) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true);

		mmh.setTo(obj.getCliente().getEmail());
		mmh.setFrom(sender);
		mmh.setSubject("Análise da ordem de serviço finalizada! Número de ordem: " +obj.getId());
		mmh.setSentDate(new Date(System.currentTimeMillis()));
		mmh.setText(htmlFromTemplateOrdemServico(obj, token), true);
		
		return mimeMessage;
	}
	
}
