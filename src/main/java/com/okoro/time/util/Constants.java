package com.okoro.time.util;

public class Constants { 

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USUARIO = "USER";
     
    public static final String TIPO_ENTRADA = "clock_in";
    public static final String TIPO_SALIDA = "clock_out";
     
    public static final String PERIODO_DIA = "dia";
    public static final String PERIODO_SEMANA = "semana";
    public static final String PERIODO_MES = "mes";
     
    public static final String FORMATO_FECHA = "yyyy-MM-dd";
    public static final String FORMATO_HORA = "HH:mm:ss";
    public static final String FORMATO_FECHA_HORA = "yyyy-MM-dd HH:mm:ss";
     
    public static final String JWT_SECRET = "okoroTimeSecretKey"; // En producción, usar variables de entorno
    public static final int JWT_EXPIRATION_MS = 86400000; // 24 horas
     
    public static final String MSG_USUARIO_NO_ENCONTRADO = "Usuario no encontrado";
    public static final String MSG_FICHAJE_NO_ENCONTRADO = "Fichaje no encontrado";
    public static final String MSG_CREDENCIALES_INVALIDAS = "Credenciales inválidas";
    public static final String MSG_NO_AUTORIZADO = "No está autorizado para realizar esta acción";
    public static final String MSG_EMAIL_YA_EXISTE = "El email ya está en uso";
}
