package ru.netology.patient.service.medical;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MedicalServiceImplTest {

    @Test
    void checkBloodPressure() {
        String id1 = "d3830f21-82e9-4b01-95f8-1dafadc38dbe";
        PatientInfo patientInfo1 = new PatientInfo(id1, "Иван", "Петров", LocalDate.of(1980, 11, 26),
                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80)));
        String id2 = "8d92aa5a-4aff-494d-ab31-78e01a7d5dc0";
        PatientInfo patientInfo2 = new PatientInfo(id2, "Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78)));
        BloodPressure currentPressure = new BloodPressure(60, 120);

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoRepository.getById(id1)).thenReturn(patientInfo1);
        Mockito.when(patientInfoRepository.getById(id2)).thenReturn(patientInfo2);

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);

        medicalService.checkBloodPressure(id1, currentPressure);
        Mockito.verify(sendAlertService).send(argumentCaptor.capture());
        Assertions.assertEquals("Warning, patient with id: d3830f21-82e9-4b01-95f8-1dafadc38dbe, need help", argumentCaptor.getValue());

        medicalService.checkBloodPressure(id2, currentPressure);
        Mockito.verify(sendAlertService, Mockito.times(2)).send(argumentCaptor.capture());
        Assertions.assertEquals("Warning, patient with id: 8d92aa5a-4aff-494d-ab31-78e01a7d5dc0, need help", argumentCaptor.getValue());
    }

    @Test
    void checkTemperature() {
        String id1 = "d3830f21-82e9-4b01-95f8-1dafadc38dbe";
        PatientInfo patientInfo1 = new PatientInfo(id1, "Иван", "Петров", LocalDate.of(1980, 11, 26),
                new HealthInfo(new BigDecimal("38.75"), new BloodPressure(120, 80)));
        String id2 = "8d92aa5a-4aff-494d-ab31-78e01a7d5dc0";
        PatientInfo patientInfo2 = new PatientInfo(id2, "Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                new HealthInfo(new BigDecimal("39.1"), new BloodPressure(125, 78)));
        BigDecimal currentTemperature = new BigDecimal("36.8");

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoRepository.getById(id1)).thenReturn(patientInfo1);
        Mockito.when(patientInfoRepository.getById(id2)).thenReturn(patientInfo2);

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);

        medicalService.checkTemperature(id1, currentTemperature);
        Mockito.verify(sendAlertService).send(argumentCaptor.capture());
        Assertions.assertEquals("Warning, patient with id: d3830f21-82e9-4b01-95f8-1dafadc38dbe, need help", argumentCaptor.getValue());

        medicalService.checkTemperature(id2, currentTemperature);
        Mockito.verify(sendAlertService, Mockito.times(2)).send(argumentCaptor.capture());
        Assertions.assertEquals("Warning, patient with id: 8d92aa5a-4aff-494d-ab31-78e01a7d5dc0, need help", argumentCaptor.getValue());
    }

    @Test
    void checkOk() {
        String id1 = "d3830f21-82e9-4b01-95f8-1dafadc38dbe";
        PatientInfo patientInfo1 = new PatientInfo(id1, "Иван", "Петров", LocalDate.of(1980, 11, 26),
                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80)));
        String id2 = "8d92aa5a-4aff-494d-ab31-78e01a7d5dc0";
        PatientInfo patientInfo2 = new PatientInfo(id2, "Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78)));
        BloodPressure currentPressure = new BloodPressure(120, 80);
        BigDecimal currentTemperature = new BigDecimal("36.8");

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoRepository.getById(id1)).thenReturn(patientInfo1);
        Mockito.when(patientInfoRepository.getById(id2)).thenReturn(patientInfo2);

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);

        medicalService.checkBloodPressure(id1, currentPressure);
        Mockito.verify(sendAlertService, Mockito.times(0)).send(Mockito.any());

        medicalService.checkTemperature(id2, currentTemperature);
        Mockito.verify(sendAlertService, Mockito.times(0)).send(Mockito.any());
    }

}