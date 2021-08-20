package com.thiagobetha.projeto_tecnico.services;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import com.thiagobetha.projeto_tecnico.domain.Funcionario;
import com.thiagobetha.projeto_tecnico.domain.OrdemServico;

public interface EmailService {
	
	void sendOrdemServicoConfirmationEmail(OrdemServico obj, String token);
	
	void sendOrdemServicoConclusionEmail(OrdemServico obj);
	
	void sendOrdemServicoCancelEmail(OrdemServico obj);
	
	void sendPasswordRequestEmail(Funcionario funcionario, String newPass);

	void sendNewPasswordEmail(Funcionario funcionario, String newPass);
	
	void sendEmail(SimpleMailMessage msg);
	
	// HTML:
	
	void sendOrdemServicoConfirmationHtmlEmail(OrdemServico obj, String token);

	void sendOrdemServicoConclusionHtmlEmail(OrdemServico obj);
	
	void sendOrdemServicoCancelHtmlEmail(OrdemServico obj);
	
	void sendHtmlEmail(MimeMessage msg);
}
