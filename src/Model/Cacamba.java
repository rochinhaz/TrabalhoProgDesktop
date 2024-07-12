/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.Serializable;
/**
 *
 * @author jefer
 */
public class Cacamba implements Serializable {
    
    private static final long serialVersionUID = 1;
    private Integer id;
    private String condicao;
    
    
    public Integer getId(){
        return id;
    }
    
    public void setId(Integer id){
        this.id = id;
    }
    
    public String getCondicao(){
        return condicao;
    }
    
    public void setCondicao(String condicao){
        this.condicao = condicao;
    }
}
