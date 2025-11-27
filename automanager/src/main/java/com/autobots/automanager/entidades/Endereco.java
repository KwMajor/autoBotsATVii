package com.autobots.automanager.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
public class Endereco {
	@Id()
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = true)
	@Size(min = 2, max = 2, message = "Estado deve ter 2 caracteres")
	private String estado;
	
	@Column(nullable = false)
	@NotBlank(message = "Cidade é obrigatória")
	private String cidade;
	
	@Column(nullable = true)
	private String bairro;
	
	@Column(nullable = false)
	@NotBlank(message = "Rua é obrigatória")
	private String rua;
	
	@Column(nullable = false)
	@NotBlank(message = "Número é obrigatório")
	private String numero;
	
	@Column(nullable = true)
	private String codigoPostal;
	
	@Column(unique = false, nullable = true)
	private String informacoesAdicionais;

}