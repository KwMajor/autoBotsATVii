package com.autobots.automanager.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
public class Telefone {
	@Id()
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	@NotBlank(message = "DDD é obrigatório")
	@Size(min = 2, max = 3, message = "DDD deve ter 2 ou 3 dígitos")
	@Pattern(regexp = "\\d+", message = "DDD deve conter apenas números")
	private String ddd;
	
	@Column
	@NotBlank(message = "Número é obrigatório")
	@Size(min = 8, max = 9, message = "Número deve ter 8 ou 9 dígitos")
	private String numero;
}