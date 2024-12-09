package project.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DocumentValidator implements ConstraintValidator<DocumentConstraint, String> {

    @Override
    public boolean isValid(String document, ConstraintValidatorContext context) {
        if (document == null || document.isEmpty()) {
            return false; // Campo obrigatório
        }
        return isValidCPF(document) || isValidCNPJ(document);
    }

    private boolean isValidCPF(String cpf) {
        cpf = cpf.replaceAll("\\D", ""); // Remove caracteres não numéricos
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int digito1 = calcularDigito(cpf, 10);
            int digito2 = calcularDigito(cpf, 11);
            return cpf.equals(cpf.substring(0, 9) + digito1 + digito2);
        } catch (Exception e) {
            return false;
        }
    }

    private int calcularDigito(String cpf, int peso) {
        int soma = 0;
        for (int i = 0; i < cpf.length() - (12 - peso); i++) {
            soma += (cpf.charAt(i) - '0') * (peso - i);
        }
        int resto = 11 - (soma % 11);
        return (resto > 9) ? 0 : resto;
    }

    private boolean isValidCNPJ(String cnpj) {
        cnpj = cnpj.replaceAll("\\D", ""); // Remove caracteres não numéricos
        if (cnpj.length() != 14) {
            return false;
        }

        try {
            int digito1 = calcularDigito(cnpj, 5, 12);
            int digito2 = calcularDigito(cnpj, 6, 13);
            return cnpj.equals(cnpj.substring(0, 12) + digito1 + digito2);
        } catch (Exception e) {
            return false;
        }
    }

    private int calcularDigito(String cnpj, int peso, int tamanho) {
        int soma = 0;
        int pos = peso;
        for (int i = 0; i < tamanho; i++) {
            soma += (cnpj.charAt(i) - '0') * pos;
            pos = (pos - 1) < 2 ? 9 : pos - 1;
        }
        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }
}
