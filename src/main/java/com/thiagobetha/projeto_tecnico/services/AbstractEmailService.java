package com.thiagobetha.projeto_tecnico.services;

import java.util.Date;

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
	
	@Value("${img.default.url}")
	private String amazonUrl;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	// ======= SIMPLE MAIL MESSAGE / MOCK EMAIL =======
	
	@Override
	public void sendOrdemServicoConfirmationEmail(OrdemServico obj, String token) {
		SimpleMailMessage sm = prepareOrdemServicoConfirmationEmail(obj, token);
		sendEmail(sm);
	}
	
	@Override
	public void sendOrdemServicoConclusionEmail(OrdemServico obj) {
		SimpleMailMessage sm = prepareOrdemServicoConclusionEmail(obj);
		sendEmail(sm);
	}
	
	@Override
	public void sendOrdemServicoCancelEmail(OrdemServico obj) {
		SimpleMailMessage sm = prepareOrdemServicoCancelEmail(obj);
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
	
	protected SimpleMailMessage prepareOrdemServicoConfirmationEmail(OrdemServico obj, String token) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(obj.getCliente().getEmail());
		sm.setFrom(sender);
		sm.setSubject("Análise da ordem de serviço finalizada! Número de ordem: " +obj.getId());
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText(htmlFromTemplateOrdemServicoConfirmation(obj, token));
		return sm;
	}

	// EMAIL DE CONCLUSAO DE ORDEM:
	protected SimpleMailMessage prepareOrdemServicoConclusionEmail(OrdemServico obj) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(obj.getCliente().getEmail());
		sm.setFrom(sender);
		sm.setSubject("Manutenção concluída! Número de ordem: " +obj.getId());
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText(
				"Olá " +obj.getCliente().getNome()+"!"+
				"\nSua ordem de serviço foi concluída e seus itens estão disponíveis para retirada!"+
				"\nDados da ordem: \n"+
				obj.toString(amazonUrl)
				);
		return sm;
	}
	
	protected SimpleMailMessage prepareOrdemServicoCancelEmail(OrdemServico obj) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(obj.getCliente().getEmail());
		sm.setFrom(sender);
		sm.setSubject("Cancelamento de ordem de serviço confirmado! Número de ordem: " +obj.getId());
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText(
				"Olá " +obj.getCliente().getNome()+"!"+
				"\nSua solicitação de cancelamento da ordem de serviço foi confirmada e seus itens estão disponíveis para retirada!"+
				"\nDados da ordem: \n"+
				obj.toString(amazonUrl)
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
	
	// ======= MIME MESSAGES / SMTP EMAIL ======= 
	
	// EMAIL DE APROVACAO/CANCELAMENTO DE ORDEM:
	@Override
	public void sendOrdemServicoConfirmationHtmlEmail(OrdemServico obj, String token) {
		try {
			MimeMessage mm = prepareMimeMessageOrdemServicoConfirmationEmail(obj, token);
			sendHtmlEmail(mm);
		} catch (MessagingException e) {
			sendOrdemServicoConfirmationEmail(obj, token);
		}
	}
	
	protected MimeMessage prepareMimeMessageOrdemServicoConfirmationEmail(OrdemServico obj, String token) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true);

		mmh.setTo(obj.getCliente().getEmail());
		mmh.setFrom(sender);
		mmh.setSubject("Análise da ordem de serviço finalizada! Número de ordem: " +obj.getId());
		mmh.setSentDate(new Date(System.currentTimeMillis()));
		mmh.setText(htmlFromTemplateOrdemServicoConfirmation(obj, token), true);
		
		return mimeMessage;
	}

	// EMAIL DE CONCLUSAO DE ORDEM:
	@Override
	public void sendOrdemServicoConclusionHtmlEmail(OrdemServico obj) {
		try {
			MimeMessage mm = prepareMimeMessageOrdemServicoConclusionEmail(obj);
			sendHtmlEmail(mm);
		} catch (MessagingException e) {
			sendOrdemServicoConclusionEmail(obj);
		}
	}
	
	protected MimeMessage prepareMimeMessageOrdemServicoConclusionEmail(OrdemServico obj) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true);

		mmh.setTo(obj.getCliente().getEmail());
		mmh.setFrom(sender);
		mmh.setSubject("Manutenção concluída! Número de ordem: " +obj.getId());
		mmh.setSentDate(new Date(System.currentTimeMillis()));
		mmh.setText(htmlFromTemplateOrdemServicoConclusion(obj), true);
		
		return mimeMessage;
	}
	
	// EMAIL DE CANCELAMENTO DE ORDEM:
	@Override
	public void sendOrdemServicoCancelHtmlEmail(OrdemServico obj) {
		try {
			MimeMessage mm = prepareMimeMessageOrdemServicoCancelEmail(obj);
			sendHtmlEmail(mm);
		} catch (MessagingException e) {
			sendOrdemServicoCancelEmail(obj);
		}
	}
	
	protected MimeMessage prepareMimeMessageOrdemServicoCancelEmail(OrdemServico obj) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true);

		mmh.setTo(obj.getCliente().getEmail());
		mmh.setFrom(sender);
		mmh.setSubject("Cancelamento de ordem de serviço confirmado! Número de ordem: " +obj.getId());
		mmh.setSentDate(new Date(System.currentTimeMillis()));
		mmh.setText(htmlFromTemplateOrdemServicoCancel(obj), true);
		
		return mimeMessage;
	}
	
	// ======= HTML TEMPLATES =======
	
	protected String htmlFromTemplateOrdemServicoConfirmation(OrdemServico obj, String token) {
		Context context = new Context();
		context.setVariable("ordemServico", obj);
		context.setVariable("token", token);
		return templateEngine.process("email/confirmacaoOrdemServico", context);
	}
	
	protected String htmlFromTemplateOrdemServicoConclusion(OrdemServico obj) {
		Context context = new Context();
		context.setVariable("ordemServico", obj);
		return templateEngine.process("email/conclusaoOrdemServico", context);
	}
	
	protected String htmlFromTemplateOrdemServicoCancel(OrdemServico obj) {
		Context context = new Context();
		context.setVariable("ordemServico", obj);
		return templateEngine.process("email/cancelamentoOrdemServico", context);
	}
}
