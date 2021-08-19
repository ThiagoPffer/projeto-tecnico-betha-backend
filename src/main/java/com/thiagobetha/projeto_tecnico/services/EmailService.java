package com.thiagobetha.projeto_tecnico.services;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import com.thiagobetha.projeto_tecnico.domain.Funcionario;
import com.thiagobetha.projeto_tecnico.domain.OrdemServico;

public interface EmailService {
	
	void sendOrderConfirmationEmail(OrdemServico obj, String token);
	
	void sendOrderConclusionEmail(OrdemServico obj);
	
	void sendCancellationConfirmationEmail(OrdemServico obj);
	
	void sendEmail(SimpleMailMessage msg);
	
	void sendPasswordRequestEmail(Funcionario funcionario, String newPass);

	void sendNewPasswordEmail(Funcionario funcionario, String newPass);
	
	// HTML:
	
	void sendOrderConfirmationHtmlEmail(OrdemServico obj, String token);

	void sendHtmlEmail(MimeMessage msg);
}
