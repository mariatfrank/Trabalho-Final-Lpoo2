package controller;

public class ClienteFormController {
    private String mensagemErro;

    public ClienteFormController(Object cliente) {
    }

    public boolean validarCampos(String nome, String sobrenome, String telefone) {
        nome = nome.trim();
        sobrenome = sobrenome.trim();
        String apenasNumeros = telefone.replaceAll("[^\\d]", "");

        if (nome.isEmpty()) {
            mensagemErro = "Nome não pode estar vazio.";
            return false;
        }
        if (!nome.matches("[A-Za-zÀ-ÿ\\s]+")) {
            mensagemErro = "O nome deve conter apenas letras e espaços.";
            return false;
        }

        if (sobrenome.isEmpty()) {
            mensagemErro = "Sobrenome não pode estar vazio.";
            return false;
        }
        if (!sobrenome.matches("[A-Za-zÀ-ÿ\\s]+")) {
            mensagemErro = "O sobrenome deve conter apenas letras e espaços.";
            return false;
        }

        if (!apenasNumeros.matches("\\d{8,15}")) {
            mensagemErro = "Telefone deve conter entre 8 e 15 dígitos numéricos.";
            return false;
        }

        return true;
    }

    public String formatarTelefone(String telefone) {
        String numeros = telefone.replaceAll("[^\\d]", "");

        if (numeros.length() == 10) {
            return String.format("(%s) %s-%s",
                    numeros.substring(0, 2),
                    numeros.substring(2, 6),
                    numeros.substring(6));
        } else if (numeros.length() == 11) {
            return String.format("(%s) %s-%s",
                    numeros.substring(0, 2),
                    numeros.substring(2, 7),
                    numeros.substring(7));
        } else {
            return telefone;
        }
    }

    public String getMensagemErro() {
        return mensagemErro;
    }
}
