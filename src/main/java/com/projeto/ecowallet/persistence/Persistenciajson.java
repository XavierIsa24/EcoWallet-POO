package com.projeto.ecowallet.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.projeto.ecowallet.model.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Persistenciajson {

    private static final String CAMINHO_ARQUIVO =
            System.getProperty("user.home") + "/EcoWallet/transacoes.json";

    private static final String CAMINHO_BACKUP =
            System.getProperty("user.home") + "/EcoWallet/transacoes.backup.json";

    private final ObjectMapper mapper;

    public Persistenciajson() {
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void salvar(List<Transacao> transacoes) {
        try {
            File arquivo = new File(CAMINHO_ARQUIVO);
            arquivo.getParentFile().mkdirs();

            if (arquivo.exists()) {
                Files.copy(arquivo.toPath(),
                        new File(CAMINHO_BACKUP).toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
            }

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(arquivo, transacoes);

            System.out.println("JSON salvo com sucesso!");

        } catch (IOException e) {
            System.out.println("Erro ao salvar JSON.");
            e.printStackTrace();
        }
    }

    public List<Transacao> carregar() {
        File arquivo = new File(CAMINHO_ARQUIVO);

        if (!arquivo.exists()) {
            return new ArrayList<>();
        }

        try {
            return mapper.readValue(
                    arquivo,
                    new TypeReference<List<Transacao>>() {}
            );

        } catch (Exception e) {
            System.out.println("Falha ao ler JSON no formato atual. Tentando formato antigo...");
            e.printStackTrace();

            return carregarFormatoAntigo(arquivo);
        }
    }

    private List<Transacao> carregarFormatoAntigo(File arquivo) {
        List<Transacao> lista = new ArrayList<>();

        try {
            List<Map<String, Object>> dados =
                    mapper.readValue(
                            arquivo,
                            new TypeReference<List<Map<String, Object>>>() {}
                    );

            for (Map<String, Object> item : dados) {

                String descricao = (String) item.get("descricao");

                double valor =
                        Double.parseDouble(item.get("valor").toString());

                TipoTransacao tipo =
                        TipoTransacao.valueOf(item.get("tipo").toString());

                Categoria categoria =
                        Categoria.valueOf(item.get("categoria").toString());

                LocalDate data = null;

                if (item.get("data") != null) {
                    Object dataObj = item.get("data");

                    if (dataObj instanceof String) {
                        data = LocalDate.parse((String) dataObj);

                    } else if (dataObj instanceof List) {
                        List<?> listaData = (List<?>) dataObj;
                        int ano = (int) listaData.get(0);
                        int mes = (int) listaData.get(1);
                        int dia = (int) listaData.get(2);
                        data = LocalDate.of(ano, mes, dia);
                    }
                }

                Transacao t = (tipo == TipoTransacao.RECEITA)
                        ? new Receita(descricao, valor, data, categoria)
                        : new Despesa(descricao, valor, data, categoria);

                lista.add(t);
            }

            salvar(lista);
            System.out.println("Formato antigo convertido com sucesso (" + lista.size() + " transações).");

            return lista;

        } catch (Exception ex) {
            System.out.println("Falha ao converter formato antigo. Arquivo original preservado em: " + CAMINHO_ARQUIVO);
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }
}