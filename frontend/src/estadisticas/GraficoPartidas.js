import React, { useState } from 'react';
import Highcharts from 'highcharts';
import HighchartsReact from 'highcharts-react-official';


const GraficoPartidas = ({ estadisticas }) => {
    const [graficoActualPartidas, setGraficoActualPartidas] = useState("resultados");

    const victoriasDerrotas = {
        chart: {
            type: 'pie',
            backgroundColor: 'rgba(0, 0, 0, 0)',
        },
        title: {
            text: 'Resultados en ' + (estadisticas.partidasJugadas || 0) + ' partidas',
            style: { color: '#ffffff' }
        },
        pane: {
            size: '80%'
        },
        series: [{
            name: 'Resultados',
            colorByPoint: true,
            data: [
                { name: 'Victorias', y: estadisticas.victorias || 0, color: '#4caf50' },
                { name: 'Derrotas', y: estadisticas.derrotas || 0, color: '#f44336' },
            ]
        }]
    };

    const tiposPartidas = {
        chart: {
            backgroundColor: 'rgba(0, 0, 0, 0)',
            plotBorderWidth: 0,
            plotShadow: false
        },
        title: {
            text: 'Tipos de Partidas',
            style: { color: '#ffffff' }
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        pane: {
            size: '80%'
        },
        series: [{
            name: 'Cantidad',
            type: 'pie',
            innerSize: '50%',
            data: [
                ['Partidas de a 2', estadisticas.partidasA2 || 0],
                ['Partidas de a 4', estadisticas.partidasA4 || 0],
                ['Partidas de a 6', estadisticas.partidasA6 || 0],
            ],
            pointPlacement: 'on',
            color: '#2196f3'
        }]
    };

    const cambiarGrafico = () => {
        setGraficoActualPartidas(graficoActualPartidas === "resultados" ? "tiposPartidas" : "resultados");
    };

    return (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
            <HighchartsReact
                highcharts={Highcharts}
                options={graficoActualPartidas === "resultados" ? victoriasDerrotas : tiposPartidas}
            />

         
          
                <button
                    onClick={cambiarGrafico}
                    style={{
                        marginTop: '20px',
                        padding: '10px 20px',
                        background: graficoActualPartidas === "resultados" ? 'linear-gradient(45deg, #2196f3, #21cbf3)' : 'linear-gradient(45deg, #4caf50, #8bc34a)',
                        color: 'white',
                        border: 'none',
                        borderRadius: '25px',
                        cursor: 'pointer',
                        transition: 'background 0.3s, transform 0.3s',
                        fontSize: '16px',
                        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
                    }}
                    onMouseEnter={(e) => {
                        e.target.style.background = graficoActualPartidas === "resultados" ? 'linear-gradient(45deg, #1e88e5, #1e88e5)' : 'linear-gradient(45deg, #45a049, #45a049)';
                        e.target.style.transform = 'scale(1.05)';
                    }}
                    onMouseLeave={(e) => {
                        e.target.style.background = graficoActualPartidas === "resultados" ? 'linear-gradient(45deg, #2196f3, #21cbf3)' : 'linear-gradient(45deg, #4caf50, #8bc34a)';
                        e.target.style.transform = 'scale(1)';
                    }}
                >
                    {graficoActualPartidas === "resultados" ? "Tipos de Partidas" : "Resultados"}
                </button>
           
        </div>
    );
};

export default GraficoPartidas;
