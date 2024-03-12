package org.apache.camel.learn;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@CsvRecord(separator = ",")
public class Persona {
    @DataField(pos = 1)
    private String nombre;
    @DataField(pos = 2)

    private String cedula;
    @DataField(pos = 3)
    private String correo;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "PERSONA "+ getNombre() +  " "+ getCedula() +  " "+ getCorreo();
    }
}
