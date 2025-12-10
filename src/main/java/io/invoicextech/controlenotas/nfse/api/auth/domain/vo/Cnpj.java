package io.invoicextech.controlenotas.nfse.api.auth.domain.vo;

import java.util.Objects;

public final class Cnpj {
    private static final String NUMBERS_ONLY = "\\d{14}";
    private final String value;

    private Cnpj(String value) {
        this.value = value;
    }

    public static Cnpj of(String input) {
        if (input == null) throw new IllegalArgumentException("CNPJ obrigatório");
        String normalized = input.replaceAll("\\D", "");
        if (!normalized.matches(NUMBERS_ONLY)) {
            throw new IllegalArgumentException("CNPJ inválido (formato)");
        }
        return new Cnpj(normalized);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cnpj cnpj)) return false;
        return Objects.equals(value, cnpj.value);
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
