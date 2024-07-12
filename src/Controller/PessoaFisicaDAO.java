/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.PessoaFisica;
/**
 *
 * @author jefer
 */
public class PessoaFisicaDAO {
    private Connection connection = null;
    private PreparedStatement pstdados = null;
    private ResultSet rsdados = null;
    private static final String path = System.getProperty("user.dir");
    private static final File config_file = new File(path + "/src/Controller/configuracaobd.properties");
    private static final String sqlinsert = "INSERT INTO PessoaFisica (cpf, nome, telefone, endereco) VALUES (?, ?, ?, ?)";
    private static final String sqlupdate = "UPDATE PessoaFisica SET nome = ?, telefone = ?, endereco = ? WHERE cpf = ?";
    private static final String sqldelete = "DELETE FROM PessoaFisica WHERE cpf = ?";
    private static final String sqlselect = "SELECT * FROM PessoaFisica WHERE cpf = ?";
    private static final String sqlselectall = "SELECT * FROM PessoaFisica";

    public PessoaFisicaDAO() {
        CriaConexao();
    }

    public boolean CriaConexao() {
        try {
            JDBCUtil.init(config_file);
            connection = JDBCUtil.getConnection();
            connection.setAutoCommit(false);
            return true;
        } catch (ClassNotFoundException | IOException | SQLException erro) {
            System.out.println("Erro ao criar conexão: " + erro);
        }
        return false;
    }

    public boolean FechaConexao() {
        if (connection != null) {
            try {
                connection.close();
                return true;
            } catch (SQLException erro) {
                System.err.println("Erro ao fechar a conexão: " + erro);
                return false;
            }
        }
        return false;
    }

    public boolean savePessoaFisica(PessoaFisica pessoaFisica) {
        try {
            pstdados = connection.prepareStatement(sqlinsert);
            pstdados.setString(1, pessoaFisica.getCpf());
            pstdados.setString(2, pessoaFisica.getNome());
            pstdados.setString(3, pessoaFisica.getTelefone());
            pstdados.setString(4, pessoaFisica.getEndereco());
            int resposta = pstdados.executeUpdate();
            pstdados.close();
            if (resposta == 1) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }
        } catch (SQLException erro) {
            System.out.println("Erro na execução da inserção: " + erro);
        }
        return false;
    }

    public boolean updatePessoaFisica(PessoaFisica pessoaFisica) {
        try {
            pstdados = connection.prepareStatement(sqlupdate);
            pstdados.setString(1, pessoaFisica.getNome());
            pstdados.setString(2, pessoaFisica.getTelefone());
            pstdados.setString(3, pessoaFisica.getEndereco());
            pstdados.setString(4, pessoaFisica.getCpf());
            int resposta = pstdados.executeUpdate();
            pstdados.close();
            if (resposta == 1) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }
        } catch (SQLException erro) {
            System.out.println("Erro na execução da atualização: " + erro);
        }
        return false;
    }

    public boolean deletePessoaFisica(String cpf) {
        try {
            pstdados = connection.prepareStatement(sqldelete);
            pstdados.setString(1, cpf);
            int resposta = pstdados.executeUpdate();
            pstdados.close();
            if (resposta == 1) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }
        } catch (SQLException erro) {
            System.out.println("Erro na execução da exclusão: " + erro);
        }
        return false;
    }

    public PessoaFisica getPessoaFisica(String cpf) {
        try {
            pstdados = connection.prepareStatement(sqlselect);
            pstdados.setString(1, cpf);
            rsdados = pstdados.executeQuery();
            if (rsdados.next()) {
                PessoaFisica pessoaFisica = new PessoaFisica();
                pessoaFisica.setCpf(rsdados.getString("cpf"));
                pessoaFisica.setNome(rsdados.getString("nome"));
                pessoaFisica.setTelefone(rsdados.getString("telefone"));
                pessoaFisica.setEndereco(rsdados.getString("endereco"));
                return pessoaFisica;
            }
        } catch (SQLException erro) {
            System.out.println("Erro na execução da consulta: " + erro);
        }
        return null;
    }

    public List<PessoaFisica> getTodasPessoasFisica() {
        List<PessoaFisica> pessoasFisica = new ArrayList<>();
        try {
            pstdados = connection.prepareStatement(sqlselectall);
            rsdados = pstdados.executeQuery();
            while (rsdados.next()) {
                PessoaFisica pessoaFisica = new PessoaFisica();
                pessoaFisica.setCpf(rsdados.getString("cpf"));
                pessoaFisica.setNome(rsdados.getString("nome"));
                pessoaFisica.setTelefone(rsdados.getString("telefone"));
                pessoaFisica.setEndereco(rsdados.getString("endereco"));
                pessoasFisica.add(pessoaFisica);
            }
        } catch (SQLException erro) {
            System.out.println("Erro na execução da consulta: " + erro);
        }
        return pessoasFisica;
    }
}
