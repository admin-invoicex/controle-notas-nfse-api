package io.invoicextech.controlenotas.nfse.api.auth.domain.vo;

import java.util.Objects;

public final class Cpf {
    private static final String NUMBERS_ONLY = "\\d{11}";

    private final String value;

    private Cpf(String value) {
        this.value = value;
    }

    public static Cpf of(String input) {
        if (input == null) throw new IllegalArgumentException("CPF obrigatório");
        String normalized = input.replaceAll("\\D", "");
        if (!normalized.matches(NUMBERS_ONLY)) {
            throw new IllegalArgumentException("CPF inválido (formato)");
        }
        return new Cpf(normalized);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cpf cpf)) return false;
        return Objects.equals(value, cpf.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
