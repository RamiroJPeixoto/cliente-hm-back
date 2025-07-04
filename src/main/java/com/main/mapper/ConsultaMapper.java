package com.main.mapper;

import com.main.domain.entity.AdministradorEntity;
import com.main.domain.entity.ConsultaRegistroEntity;
import com.main.domain.entity.MedicoEntity;
import com.main.domain.entity.SinaisVitaisEntity;
import com.main.api.model.ConsultaDTO;
import com.main.api.model.CriarConsultaRequestDTO;
import com.main.api.model.AtualizarConsultaRequestDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ConsultaMapper {

    private final ModelMapper modelMapper;
    private final SinaisVitaisMapper sinaisVitaisMapper;

    @Autowired
    public ConsultaMapper(ModelMapper modelMapper, SinaisVitaisMapper sinaisVitaisMapper) {
        this.modelMapper = modelMapper;
        this.sinaisVitaisMapper = sinaisVitaisMapper;
    }

    public ConsultaRegistroEntity toEntity(CriarConsultaRequestDTO dto) {
        if (dto == null) return null;
        ConsultaRegistroEntity entity = new ConsultaRegistroEntity();
        entity.setMotivoConsulta(dto.getMotivoConsulta());
        entity.setQueixasPrincipais(dto.getQueixasPrincipais());
        entity.setExameFisico(dto.getExameFisico());
        entity.setHipoteseDiagnostica(dto.getHipoteseDiagnostica());
        entity.setCondutaPlanoTerapeutico(dto.getCondutaPlanoTerapeutico());
        entity.setDetalhesConsulta(dto.getDetalhesConsulta());
        entity.setObservacoesConsulta(dto.getObservacoesConsulta());
        entity.setDataConsulta(dto.getDataConsulta());

        if (dto.getSinaisVitais() != null) {
            SinaisVitaisEntity sinaisVitaisEntity = sinaisVitaisMapper.toEntity(dto.getSinaisVitais());
            entity.setSinaisVitais(sinaisVitaisEntity);
        }
        return entity;
    }

    public ConsultaDTO toDTO(ConsultaRegistroEntity entity) {
        if (entity == null) return null;

        ConsultaDTO dto = modelMapper.map(entity, ConsultaDTO.class);
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setDataConsulta(entity.getDataConsulta());

        if (entity.getProntuario() != null) {
            dto.setProntuarioId(entity.getProntuario().getId());
        }

        if (entity.getSinaisVitais() != null) {
            dto.setSinaisVitais(sinaisVitaisMapper.toDTO(entity.getSinaisVitais()));
        }

        if (entity.getResponsavelMedico() != null) {
            dto.setResponsavelId(entity.getResponsavelMedico().getId());
            dto.setResponsavelNomeCompleto(entity.getResponsavelMedico().getNomeCompleto());
            dto.setResponsavelEspecialidade(entity.getResponsavelMedico().getEspecialidade());
            dto.setResponsavelCRM(entity.getResponsavelMedico().getCrm());
        }
        return dto;
    }

    public void updateEntityFromDTO(AtualizarConsultaRequestDTO dto, ConsultaRegistroEntity entity, MedicoEntity medicoExecutor, AdministradorEntity adminLogado) {
        if (dto == null || entity == null) return;

        if (StringUtils.hasText(dto.getMotivoConsulta())) entity.setMotivoConsulta(dto.getMotivoConsulta());
        if (StringUtils.hasText(dto.getQueixasPrincipais())) entity.setQueixasPrincipais(dto.getQueixasPrincipais());
        if (dto.getDataConsulta() != null) entity.setDataConsulta(dto.getDataConsulta());

        if (dto.getSinaisVitais() != null) {
            SinaisVitaisEntity sinaisVitaisEntity = entity.getSinaisVitais();
            if (sinaisVitaisEntity == null) {
                sinaisVitaisEntity = new SinaisVitaisEntity();
                entity.setSinaisVitais(sinaisVitaisEntity);
            }
            sinaisVitaisMapper.updateEntityFromDTO(dto.getSinaisVitais(), sinaisVitaisEntity);
        } else if (entity.getSinaisVitais() != null) {
        }

        if (dto.getExameFisico() != null) entity.setExameFisico(StringUtils.hasText(dto.getExameFisico()) ? dto.getExameFisico().trim() : null);
        if (dto.getHipoteseDiagnostica() != null) entity.setHipoteseDiagnostica(StringUtils.hasText(dto.getHipoteseDiagnostica()) ? dto.getHipoteseDiagnostica().trim() : null);
        if (dto.getCondutaPlanoTerapeutico() != null) entity.setCondutaPlanoTerapeutico(StringUtils.hasText(dto.getCondutaPlanoTerapeutico()) ? dto.getCondutaPlanoTerapeutico().trim() : null);
        if (dto.getDetalhesConsulta() != null) entity.setDetalhesConsulta(StringUtils.hasText(dto.getDetalhesConsulta()) ? dto.getDetalhesConsulta().trim() : null);
        if (dto.getObservacoesConsulta() != null) entity.setObservacoesConsulta(StringUtils.hasText(dto.getObservacoesConsulta()) ? dto.getObservacoesConsulta().trim() : null);

        if (medicoExecutor != null) {
            entity.setResponsavelMedico(medicoExecutor);
        }
    }
}