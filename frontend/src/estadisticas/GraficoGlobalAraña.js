import React from 'react';
import HighchartsReact from 'highcharts-react-official';
import HighchartsMore from 'highcharts/highcharts-more';
import Highcharts from 'highcharts';
import useFetchState from '../util/useFetchState';

const GraficoGlobalAraña = ({ estadisticas, jwt,setMessage,setVisible }) => {

const [estadisticasGlobales, setEstadisticasGlobales] = useFetchState({}, '/api/v1/estadisticas/estadisticasGlobales', jwt, setMessage, setVisible);
 const graficoGlobalAraña = {
        chart: {
            polar: true,
            type: 'line',
            backgroundColor: 'rgba(0, 0, 0, 0)',
        },
        title: {
            text: 'Comparativa con el promedio global',
            x: -80,
            style: { color: '#ffffff' }
        },
        pane: {
            size: '80%'
        },
        xAxis: {
            categories: [
                'Victorias', 'Derrotas',
                'Flores cantadas', 'Partidas a 2',
                'Partidas a 4', 'Partidas a 6'
            ],
            tickmarkPlacement: 'on',
            lineWidth: 0,
            labels: {
                style: { color: '#ffffff' }
            }
        },
        yAxis: {
            gridLineInterpolation: 'polygon',
            lineWidth: 0,
            min: 0,
            labels: {
                style: { color: '#ffffff' }
            }
        },
        tooltip: {
            shared: true,
            pointFormat: '<span style="color:{series.color}">{series.name}: <b> {point.y:,.3f}</b><br/>'
        },
        legend: {
            align: 'right',
            verticalAlign: 'middle',
            layout: 'vertical'
        },
        series: [
            {
                name: 'Promedio Personal',
                data: [
                    estadisticas.victorias / estadisticas.partidasJugadas || 0,
                    estadisticas.derrotas / estadisticas.partidasJugadas || 0,
                    estadisticas.floresCantadas / estadisticas.partidasConFlor || 0,
                    estadisticas.partidasA2 / (estadisticas.partidasJugadas * 2) || 0,
                    estadisticas.partidasA4 / (estadisticas.partidasJugadas * 4) || 0,
                    estadisticas.partidasA6 / (estadisticas.partidasJugadas * 6) || 0,
                ],
                pointPlacement: 'on',
                color: '#4caf50',
                lineWidth: 3,
                marker: {
                    enabled: true
                }
            },
            {
                name: 'Promedio Global',
                data: [
                    estadisticasGlobales.victorias / estadisticasGlobales.partidasJugadas || 0,
                    estadisticasGlobales.derrotas / estadisticasGlobales.partidasJugadas || 0,
                    estadisticasGlobales.floresCantadas / estadisticasGlobales.partidasConFlor || 0,
                    estadisticasGlobales.partidasA2 / estadisticasGlobales.partidasJugadas || 0,
                    estadisticasGlobales.partidasA4 / estadisticasGlobales.partidasJugadas || 0,
                    estadisticasGlobales.partidasA6 / estadisticasGlobales.partidasJugadas || 0,
                ],
                pointPlacement: 'on',
                color: '#2196f3',
                lineWidth: 3,
                marker: {
                    enabled: true
                }
            },
        ],
        responsive: {
            rules: [{
                condition: {
                    maxWidth: 500
                },
                chartOptions: {
                    title: {
                        x: 0
                    },
                    legend: {
                        align: 'center',
                        verticalAlign: 'bottom',
                        layout: 'horizontal'
                    },
                    pane: {
                        size: '70%'
                    }
                }
            }]
        }
    };


    return (
        <HighchartsReact
            highcharts={HighchartsMore}
            options={graficoGlobalAraña}
        />
    );
};

export default GraficoGlobalAraña;