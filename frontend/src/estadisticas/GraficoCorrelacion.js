import React from 'react';
import HighchartsReact from 'highcharts-react-official';
import Highcharts from 'highcharts';

const GraficoCorrelacion = ({ estadisticasAvanzadas }) => { 
    const parsearDatosDeCorrelacion = (data) => {
        return data.map((item) => ({
            fecha: new Date(item.fecha),
            victoriasEnganos: item.victorioso ? item.enganos : 0,
            derrotasAtrapados: !item.victorioso ? item.atrapados : 0,
        }));
    };

    const datosCorrelacion = parsearDatosDeCorrelacion(estadisticasAvanzadas);

    const graficoCorrelacion = {
        chart: {
            type: 'scatter',
            backgroundColor: 'rgba(0, 0, 0, 0)',
            zoomType: 'xy'
        },
        title: {
            text: 'Correlación: Victorias y Derrotas',
            style: { color: '#ffffff' }
        },
        xAxis: {
            type: 'datetime',
            title: {
                text: 'Fecha y Hora',
                style: { color: '#ffffff' }
            },
            labels: {
                style: { color: '#ffffff' }
            }
        },
        yAxis: {
            title: {
                text: 'Cantidad',
                style: { color: '#ffffff' }
            },
            labels: {
                style: { color: '#ffffff' }
            }
        },
        tooltip: {
            headerFormat: '<b>{series.name}</b><br>',
            pointFormat: 'Fecha: {point.x:%e %b %Y, %H:%M}<br>Valor: {point.y}'
        },
        series: [
            {
                name: 'Victorias con Engaños',
                data: datosCorrelacion
                    .filter(item => item.victoriasEnganos > 0)
                    .map(item => [item.fecha.getTime(), item.victoriasEnganos]),
                color: '#4caf50',
                marker: {
                    symbol: 'circle',
                    radius: 5
                }
            },
            {
                name: 'Derrotas siendo atrapado',
                data: datosCorrelacion
                    .filter(item => item.derrotasAtrapados > 0)
                    .map(item => [item.fecha.getTime(), item.derrotasAtrapados]),
                color: '#f44336',
                marker: {
                    symbol: 'triangle',
                    radius: 5
                }
            }
        ]
    };

    
    return (
        <HighchartsReact
            highcharts={Highcharts}
            options={graficoCorrelacion}
        />
    );
};

export default GraficoCorrelacion;