package com.thiagobetha.projeto_tecnico.services;

import org.springframework.mail.SimpleMailMessage;

import com.thiagobetha.projeto_tecnico.domain.Funcionario;
import com.thiagobetha.projeto_tecnico.domain.OrdemServico;

public interface EmailService {
	
	void sendOrderConfirmationEmail(OrdemServico obj);
	
	void sendOrderConclusionEmail(OrdemServico obj);
	
	void sendCancellationConfirmationEmail(OrdemServico obj);
	
	void sendEmail(SimpleMailMessage msg);
	
	void sendNewPasswordEmail(Funcionario funcionario, String newPass);
}
