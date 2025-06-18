package model;

import java.util.Objects;

public class Cliente {
    private int id;
    private String nome;
    private String sobrenome;
    private String telefone;

    public Cliente() {}

    public Cliente(int id, String nome, String sobrenome, String telefone) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.telefone = telefone;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSobrenome() { return sobrenome; }
    public void setSobrenome(String sobrenome) { this.sobrenome = sobrenome; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    @Override
    public String toString() {
        return nome + " " + sobrenome + " - " + telefone;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente)) return false;
        Cliente c = (Cliente) o;
        return this.id == c.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
