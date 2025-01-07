import React from 'react';
import HighchartsReact from 'highcharts-react-official';
import Highcharts from 'highcharts';

const GraficoBurbujas = ({ estadisticasAvanzadas, estadisticas }) => {
    // Filtrar partidas con flor
    const partidasConFlor = estadisticasAvanzadas.filter(item => item.conFlor);

    // Cálculos para victorias
    const partidasConFlorGanadas = partidasConFlor.filter(item => item.victorioso);
    const victoriasEnPartidasConFlor = partidasConFlorGanadas.length;
    const floresCantadasEnVictorias = partidasConFlorGanadas.reduce((acc, item) => acc + item.floresCantadas, 0);
    const promedioFloresPorVictoria = victoriasEnPartidasConFlor > 0
        ? floresCantadasEnVictorias / victoriasEnPartidasConFlor
        : 0;

    // Cálculos para derrotas
    const partidasConFlorPerdidas = partidasConFlor.filter(item => !item.victorioso);
    const derrotasEnPartidasConFlor = partidasConFlorPerdidas.length;
    const floresCantadasEnDerrotas = partidasConFlorPerdidas.reduce((acc, item) => acc + item.floresCantadas, 0);
    const promedioFloresPorDerrota = derrotasEnPartidasConFlor > 0
        ? floresCantadasEnDerrotas / derrotasEnPartidasConFlor
        : 0;

    // Datos para las series
    const datosFloresVictorias = [{
        x: victoriasEnPartidasConFlor,
        y: promedioFloresPorVictoria,
        z: floresCantadasEnVictorias,
    }];

    const datosFloresDerrotas = [{
        x: derrotasEnPartidasConFlor,
        y: promedioFloresPorDerrota,
        z: floresCantadasEnDerrotas,
    }];

    // Configuración del gráfico
    const graficoBurbujas = {
        chart: {
            type: 'bubble',
            plotBorderWidth: 1,
            zoomType: 'xy',
            backgroundColor: 'rgba(0, 0, 0, 0)'
        },
        title: {
            text: 'Relación Flores/Victorias y Flores/Derrotas',
            style: { color: '#ffffff', fontSize: '20px' }
        },
        xAxis: {
            title: {
                text: 'Cantidad de Partidas',
                style: { color: '#ffffff' }
            },
            labels: {
                style: { color: '#ffffff' }
            }
        },
        yAxis: {
            title: {
                text: 'Promedio de Flores Cantadas',
                style: { color: '#ffffff' }
            },
            labels: {
                style: { color: '#ffffff' }
            }
        },
        tooltip: {
            useHTML: true,
            pointFormat: `
                <b>{series.name}</b><br>
                Partidas: <b>{point.x}</b><br>
                Promedio de Flores: <b>{point.y:.2f}</b><br>
                Total de Flores Cantadas: <b>{point.z}</b>
            `
        },
        series: [
            {
                name: 'Flores/Victorias',
                data: datosFloresVictorias,
                color: '#4caf50', 
                marker: {
                    lineWidth: 2,
                    lineColor: '#ffffff'
                }
            },
            {
                name: 'Flores/Derrotas',
                data: datosFloresDerrotas,
                color: '#f44336', 
                marker: {
                    lineWidth: 2,
                    lineColor: '#ffffff'
                }
            }
        ]
    };

    return (
        <HighchartsReact
            highcharts={Highcharts}
            options={graficoBurbujas}
        />
    );
};

export default GraficoBurbujas;
