import React, { forwardRef } from 'react';

const GestosMenu = forwardRef((props, ref) => {
    return (
        <div
            ref={ref}
            style={{
                display: "grid",
                gridTemplateColumns: "repeat(3, 1fr)", // Tres columnas con ancho igual
                gap: "2px", // Espacio reducido entre los elementos
                justifyContent: "center",
                backgroundColor: "#D2B48C", // Color marrón suave
                borderRadius: "10px", // Bordes redondeados
                padding: "5px", // Espaciado interno reducido
                boxShadow: "0px 0px 10px rgba(0, 0, 0, 0.1)", // Sombra para darle un poco de profundidad
                width: "fit-content", // Ajusta el ancho al contenido
                margin: "0 auto" // Centra el contenedor horizontalmente
            }}
        >
            {props.gestosDisp.map((gesto) => {
                return (
                    <div
                        className="gesto"
                        key={gesto.codigo}
                        style={{
                            display: "flex",
                            flexDirection: "column",
                            alignItems: "center",
                            padding: "5px", // Padding reducido
                            borderRadius: "5px",
                            transition: "transform 0.2s, box-shadow 0.2s",
                            cursor: "pointer"
                        }}
                        onMouseEnter={(e) => {
                            e.currentTarget.style.transform = "scale(1.1)";
                            e.currentTarget.style.boxShadow = "0px 0px 15px rgba(0, 0, 0, 0.2)";
                        }}
                        onMouseLeave={(e) => {
                            e.currentTarget.style.transform = "scale(1)";
                            e.currentTarget.style.boxShadow = "0px 0px 10px rgba(0, 0, 0, 0.1)";
                        }}
                        onMouseDown={() => props.enviarGesto(gesto.photo)} // Enviar gesto al pulsar
                        onMouseUp={() => props.enviarGesto(props.imagenOriginal)} // Enviar foto original al soltar
                    >
                        <img src={gesto.photo} alt={gesto.nombre} style={{ width: "40px", height: "40px" }} /> {/* Tamaño reducido */}
                        <p style={{ fontSize: "12px", margin: "2px 0 0 0" }}>{gesto.nombre}</p> {/* Tamaño de fuente reducido */}
                    </div>
                );
            })}
        </div>
    );
});

export default GestosMenu;