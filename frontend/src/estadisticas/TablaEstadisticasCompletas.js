import React from 'react';

import useFetchState from '../util/useFetchState.js';
import { calcularTiempo } from './calcularTiempo.js';

const TablaEstadisticasCompleta = ({ jwt, setMessage, setVisible, setMostrarTablaEstadisticas, estadisticas }) => {
    const [estadisticasGlobales, setEstadisticasGlobales] = useFetchState({}, '/api/v1/estadisticas/estadisticasGlobales', jwt, setMessage, setVisible);

    // Función para calcular estadísticas como promedio, máximo y mínimo
    const calcularEstadisticas = (key) => {
        let total = estadisticas[key];
        let partidasJugadas = estadisticas.partidasJugadas || 1; // Evita división entre 0
        let promedio = total / partidasJugadas;
        let maximo = estadisticasGlobales[key] || 0; // Estadística global máxima
        let minimo = estadisticas[key] === 0 ? 0 : estadisticasGlobales[key]; // Mínimo según global
        if(key === "tiempoJugado"){
            total = calcularTiempo(total)
            promedio = calcularTiempo(promedio)
            maximo = calcularTiempo(maximo)
            minimo = calcularTiempo(minimo)
        }else{
            promedio = promedio.toFixed(2)
        }
        return { total, promedio, maximo, minimo };
    };

    // Campos a mostrar
    const campos = [
        "partidasJugadas", "tiempoJugado", "victorias", "derrotas", "partidasA2", "partidasA4",
        "partidasA6", "numeroFlores", "numeroEnganos", "quieros", "noQuieros", "partidasConFlor", "atrapado"
    ];

    return (
        <div style={{
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'center',
            alignItems: 'center',
            height: '100vh',
            backgroundColor: 'rgb(0,0,0,0.8)',
            color: 'white',
            padding: '20px',
        }}>
            <table style={{
                borderCollapse: 'collapse',
                width: '80%',
                marginBottom: '20px',
                color: 'white',
                textAlign: 'left',
            }}>
                <thead>
                    <tr>
                        <th style={{ border: '1px solid white', padding: '10px' }}>Estadística</th>
                        <th style={{ border: '1px solid white', padding: '10px' }}>Total</th>
                        <th style={{ border: '1px solid white', padding: '10px' }}>Promedio</th>
                        <th style={{ border: '1px solid white', padding: '10px' }}>Máximo</th>
                        <th style={{ border: '1px solid white', padding: '10px' }}>Mínimo</th>
                    </tr>
                </thead>
                <tbody>
                    {campos.map((campo) => {
                        const { total, promedio, maximo, minimo } = calcularEstadisticas(campo);
                        return (
                            <tr key={campo}>
                                <td style={{ border: '1px solid white', padding: '10px' }}>{campo}</td>
                                <td style={{ border: '1px solid white', padding: '10px' }}>{total}</td>
                                <td style={{ border: '1px solid white', padding: '10px' }}>{promedio}</td>
                                <td style={{ border: '1px solid white', padding: '10px' }}>{maximo}</td>
                                <td style={{ border: '1px solid white', padding: '10px' }}>{minimo}</td>
                            </tr>
                        );
                    })}
                </tbody>
            </table>

            <button
                onClick={() => setMostrarTablaEstadisticas(false)}
                style={{
                    padding: '10px 20px',
                    backgroundColor: '#309e94',
                    color: 'white',
                    border: 'none',
                    borderRadius: '25px',
                    cursor: 'pointer',
                    transition: 'background-color 0.3s, transform 0.3s',
                    fontSize: '16px',
                    marginTop: '10px',
                    boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
                    width: '10%',
                    textAlign: 'center',
                }}
                onMouseEnter={(e) => {
                    e.target.style.backgroundColor = '#357a74';
                    e.target.style.transform = 'scale(1.05)';
                }}
                onMouseLeave={(e) => {
                    e.target.style.backgroundColor = '#309e94';
                    e.target.style.transform = 'scale(1)';
                }}
            >
                Volver
            </button>
        </div>
    );
};

export default TablaEstadisticasCompleta;
