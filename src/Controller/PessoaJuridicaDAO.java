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

import Model.PessoaJuridica;
import java.io.File;
/**
 *
 * @author jefer
 */
public class PessoaJuridicaDAO {
    private Connection connection = null;
    private PreparedStatement pstdados = null;
    private ResultSet rsdados = null;
    private static final String path = System.getProperty("user.dir");
    private static final File config_file = new File(path + "/src/Controller/configuracaobd.properties");
    private static final String sqlinsert = "INSERT INTO PessoaJuridica (cnpj, nome, telefone, endereco) VALUES (?, ?, ?, ?)";
    private static final String sqlupdate = "UPDATE PessoaJuridica SET nome = ?, telefone = ?, endereco = ? WHERE cnpj = ?";
    private static final String sqldelete = "DELETE FROM PessoaJuridica WHERE cnpj = ?";
    private static final String sqlselect = "SELECT * FROM PessoaJuridica WHERE cnpj = ?";
    private static final String sqlselectall = "SELECT * FROM PessoaJuridica";

    public PessoaJuridicaDAO() {
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

    public boolean savePessoaJuridica(PessoaJuridica pessoaJuridica) {
        try {
            pstdados = connection.prepareStatement(sqlinsert);
            pstdados.setString(1, pessoaJuridica.getCnpj());
            pstdados.setString(2, pessoaJuridica.getNome());
            pstdados.setString(3, pessoaJuridica.getTelefone());
            pstdados.setString(4, pessoaJuridica.getEndereco());
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

    public boolean updatePessoaJuridica(PessoaJuridica pessoaJuridica) {
        try {
            pstdados = connection.prepareStatement(sqlupdate);
            pstdados.setString(1, pessoaJuridica.getNome());
            pstdados.setString(2, pessoaJuridica.getTelefone());
            pstdados.setString(3, pessoaJuridica.getEndereco());
            pstdados.setString(4, pessoaJuridica.getCnpj());
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

    public boolean deletePessoaJuridica(String cnpj) {
        try {
            pstdados = connection.prepareStatement(sqldelete);
            pstdados.setString(1, cnpj);
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

    public PessoaJuridica getPessoaJuridica(String cnpj) {
        try {
            pstdados = connection.prepareStatement(sqlselect);
            pstdados.setString(1, cnpj);
            rsdados = pstdados.executeQuery();
            if (rsdados.next()) {
                PessoaJuridica pessoaJuridica = new PessoaJuridica();
                pessoaJuridica.setCnpj(rsdados.getString("cnpj"));
                pessoaJuridica.setNome(rsdados.getString("nome"));
                pessoaJuridica.setTelefone(rsdados.getString("telefone"));
                pessoaJuridica.setEndereco(rsdados.getString("endereco"));
                return pessoaJuridica;
            }
        } catch (SQLException erro) {
            System.out.println("Erro na execução da consulta: " + erro);
        }
        return null;
    }

    public List<PessoaJuridica> getTodasPessoasJuridica() {
        List<PessoaJuridica> pessoasJuridica = new ArrayList<>();
        try {
            pstdados = connection.prepareStatement(sqlselectall);
            rsdados = pstdados.executeQuery();
            while (rsdados.next()) {
                PessoaJuridica pessoaJuridica = new PessoaJuridica();
                pessoaJuridica.setCnpj(rsdados.getString("cnpj"));
                pessoaJuridica.setNome(rsdados.getString("nome"));
                pessoaJuridica.setTelefone(rsdados.getString("telefone"));
                pessoaJuridica.setEndereco(rsdados.getString("endereco"));
                pessoasJuridica.add(pessoaJuridica);
            }
        } catch (SQLException erro) {
            System.out.println("Erro na execução da consulta: " + erro);
        }
        return pessoasJuridica;
    }
}
