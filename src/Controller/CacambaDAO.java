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

import Model.Cacamba;
/**
 *
 * @author jefer
 */
public class CacambaDAO {
    private Connection connection = null;
    private PreparedStatement pstdados = null;
    private ResultSet rsdados = null;
    private static final String path = System.getProperty("user.dir");
    private static final File config_file = new File(path + "/src/Controller/configuracaobd.properties");
    private static final String sqlconsultatodas = "SELECT * FROM Cacamba";
    private static final String sqlinserir = "INSERT INTO Cacamba (id, condicao) VALUES (?, ?)";
    private static final String sqlalterar = "UPDATE Cacamba SET condicao = ? WHERE id = ?";
    private static final String sqlexcluir = "DELETE FROM Cacamba WHERE id = ?";

    public CacambaDAO() {
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

    public boolean saveCacamba(Cacamba cacamba) {
        try {
            pstdados = connection.prepareStatement(sqlinserir);
            pstdados.setInt(1, cacamba.getId());
            pstdados.setString(2, cacamba.getCondicao());
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

    public boolean updateCacamba(Cacamba cacamba) {
        try {
            pstdados = connection.prepareStatement(sqlalterar);
            pstdados.setString(1, cacamba.getCondicao());
            pstdados.setInt(2, cacamba.getId());
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

    public boolean deleteCacamba(int id) {
        try {
            pstdados = connection.prepareStatement(sqlexcluir);
            pstdados.setInt(1, id);
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

    public Cacamba getCacamba(int id) {
        String query = "SELECT * FROM Cacamba WHERE id = ?";
        try {
            pstdados = connection.prepareStatement(query);
            pstdados.setInt(1, id);
            rsdados = pstdados.executeQuery();
            if (rsdados.next()) {
                Cacamba cacamba = new Cacamba();
                cacamba.setId(rsdados.getInt("id"));
                cacamba.setCondicao(rsdados.getString("condicao"));
                return cacamba;
            }
        } catch (SQLException erro) {
            System.out.println("Erro na execução da consulta: " + erro);
        }
        return null;
    }

    public List<Cacamba> getTodasCacambas() {
        List<Cacamba> cacambas = new ArrayList<>();
        try {
            pstdados = connection.prepareStatement(sqlconsultatodas);
            rsdados = pstdados.executeQuery();
            while (rsdados.next()) {
                Cacamba cacamba = new Cacamba();
                cacamba.setId(rsdados.getInt("id"));
                cacamba.setCondicao(rsdados.getString("condicao"));
                cacambas.add(cacamba);
            }
        } catch (SQLException erro) {
            System.out.println("Erro na execução da consulta: " + erro);
        }
        return cacambas;
    }
}
