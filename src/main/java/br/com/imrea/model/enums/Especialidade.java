package br.com.imrea.model.enums;

public enum Especialidade {
    FISIATRIA("Fisiatria"),
    FISIOTERAPIA("Fisioterapia"),
    FONOAUDIOLOGIA("Fonoaudiologia"),
    T_O("Terapia Ocupacional"),
    ORTOPEDIA("Ortopedia"),
    PED_REAB("Pediatria de Reabilitação"),
    REUMATOLOGIA("Reumatologia"),
    PSICOLOGIA("Psicologia"),
    PSIQUIATRIA("Psiquiatria");

    private final String descricao;

    private Especialidade(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public String toString() {
        return this.descricao;
    }
}
