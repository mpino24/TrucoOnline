import React from 'react';
import HighchartsReact from 'highcharts-react-official';
import Highcharts from 'highcharts';

const GraficoColumnas = ({ estadisticasAvanzadas, estadisticas }) => {
   
    const partidasConFlor = estadisticasAvanzadas.filter(item => item.conFlor);

    const totalPartidasConFlor = estadisticas.partidasConFlor
    const partidasConFlorGanadas = partidasConFlor.filter(item => item.victorioso);
    const victoriasEnPartidasConFlor = partidasConFlorGanadas.length;
    const floresCantadasEnVictorias = partidasConFlorGanadas.reduce((acc, item) => acc + item.floresCantadas, 0);
    const promedioFloresPorVictoria = victoriasEnPartidasConFlor > 0
        ? floresCantadasEnVictorias / victoriasEnPartidasConFlor
        : 0;

    const partidasConFlorPerdidas = partidasConFlor.filter(item => !item.victorioso);
    const derrotasEnPartidasConFlor = partidasConFlorPerdidas.length;
    const floresCantadasEnDerrotas = partidasConFlorPerdidas.reduce((acc, item) => acc + item.floresCantadas, 0);
    const promedioFloresPorDerrota = derrotasEnPartidasConFlor > 0
        ? floresCantadasEnDerrotas / derrotasEnPartidasConFlor
        : 0;

    // Calcular los promedios de victorias y derrotas en partidas con flor
    const promedioGanadas = totalPartidasConFlor > 0
        ? victoriasEnPartidasConFlor / totalPartidasConFlor
        : 0;

    const promedioPerdidas = totalPartidasConFlor > 0
        ? derrotasEnPartidasConFlor / totalPartidasConFlor
        : 0;

    const graficoColumnas = {
        chart: {
            type: 'column',
            backgroundColor: 'rgba(0, 0, 0, 0)' 
        },
        title: {
            text: 'Relación Flores Cantadas en Victorias y Derrotas',
            style: { color: '#ffffff', fontSize: '20px' }
        },
        xAxis: {
            categories: ['Victorias', 'Derrotas'], 
            title: {
                text: 'Resultados',
                style: { color: '#ffffff' }
            },
            labels: {
                style: { color: '#ffffff' }
            }
        },
        yAxis: {
            min: 0,
            title: {
                text: 'Cantidad de Flores Cantadas / Partidas Jugadas',
                style: { color: '#ffffff' }
            },
            labels: {
                style: { color: '#ffffff' }
            },
            stackLabels: {
                enabled: true,
                style: {
                    fontWeight: 'bold',
                    color: 'white'
                }
            }
        },
        tooltip: {
            useHTML: true,
            formatter: function () {
                if (this.series.name === 'Flores Cantadas') {
                    const promedioFlores = this.x === 0 ? promedioFloresPorVictoria : promedioFloresPorDerrota;
                    const resultado = this.x === 0 ? "victoria" : "derrota";
                    return `
                        <b>${this.series.name}</b><br>
                        Promedio de ${promedioFlores.toFixed(2)} Flores cantadas por partida en ${resultado}
                    `;
                } else {
                    return `
                        <b>${this.series.name}</b><br>
                        Promedio de ${this.x === 0 
                            ? `${promedioGanadas.toFixed(2)} victorias en partidas con flor de ${partidasConFlor.length} partidas con flor totales` 
                            : `${promedioPerdidas.toFixed(2)} derrotas en partidas con flor de ${partidasConFlor.length} partidas con flor totales`
                        }
                    `;
                }
            }
        },
        plotOptions: {
            column: {
                stacking: 'normal',
                dataLabels: {
                    enabled: true,
                    color: '#ffffff',
                    formatter: function () {
                        if (this.series.name === 'Flores Cantadas') {
                            return `${this.y} flores`;
                        }
                        return `${this.y} partidas`;
                    }
                }
            }
        },
        series: [
            {
                name: 'Flores Cantadas',
                data: [
                    { y: floresCantadasEnVictorias, promedioFlores: promedioFloresPorVictoria },
                    { y: floresCantadasEnDerrotas, promedioFlores: promedioFloresPorDerrota }
                ],
                color: '#2979ff', 
            },
            {
                name: 'Partidas Jugadas con Flor',
                data: [
                    { y: victoriasEnPartidasConFlor, promedioFlores: promedioFloresPorVictoria },
                    { y: derrotasEnPartidasConFlor, promedioFlores: promedioFloresPorDerrota }
                ],
                color: '#6161b1', 
            },
        ]
    };

    return (
        <HighchartsReact
            highcharts={Highcharts}
            options={graficoColumnas}
        />
    );
};

export default GraficoColumnas;
