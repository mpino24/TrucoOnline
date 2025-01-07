import React from 'react';
import HighchartsReact from 'highcharts-react-official';
import Highcharts from 'highcharts';

const GraficoBurbujas = ({ estadisticasAvanzadas, estadisticas }) => {

    const victoriasEnPartidasConFlor = estadisticasAvanzadas.filter(item => item.conFlor && item.victorioso).length;
    const partidasConFlor = estadisticas.partidasConFlor;
    const floresCantadas = estadisticas.floresCantadas;


    const promedioFloresPorPartidaConFlor = partidasConFlor > 0 ? floresCantadas / partidasConFlor : 0;

    const datosBurbujas = [
        {
            x: partidasConFlor,
            y: promedioFloresPorPartidaConFlor,
            z: floresCantadas,
            name: 'Partidas con Flor',
            victorioso: victoriasEnPartidasConFlor
        }
    ];


    const graficoBurbujas = {
        chart: {
            type: 'bubble',
            plotBorderWidth: 1,
            zoomType: 'xy',
            backgroundColor: 'rgba(0, 0, 0, 0)'
        },
        title: {
            text: 'Relación entre Partidas Flores Cantadas y Victorias',
            style: { color: '#ffffff' }
        },
        xAxis: {
            title: {
                text: 'Total Partidas con Flor',
                style: { color: '#ffffff' }
            },
            labels: {
                style: { color: '#ffffff' }
            }
        },
        yAxis: {
            title: {
                text: 'Promedio de Flores Cantadas por Partida con Flor',
                style: { color: '#ffffff' }
            },
            labels: {
                style: { color: '#ffffff' }
            }
        },
        tooltip: {
            pointFormat: 'Partidas con Flor: {point.x}<br>' +
                'Promedio de Flores Cantadas: {point.y:.2f}<br>' +
                'Total de Flores Cantadas: {point.z}<br>' +
                'Victorias en Partidas con Flor: {point.victorioso}'
        },
        series: [{
            data: datosBurbujas.map(item => ({
                x: item.x,
                y: item.y,
                z: item.z,
                victorioso: item.victorioso,
                marker: {
                    fillColor: item.victorioso > 0 ? '#4caf50' : '#f44336',
                    lineColor: '#ffffff'
                }
            })),
            name: 'Relación Flores/Victorias',
        }]
    };


    return (
        <HighchartsReact
            highcharts={Highcharts}
            options={graficoBurbujas}
        />
    );
};

export default GraficoBurbujas;
