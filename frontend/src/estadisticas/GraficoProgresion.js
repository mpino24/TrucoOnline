import React from 'react';
import HighchartsReact from 'highcharts-react-official';
import Highcharts from 'highcharts';

const GraficoProgresion = ({ estadisticasAvanzadas }) => {

    const parsearDatosDeProgresion = (data) => {
        let victorias = 0;
        let derrotas = 0;
        return data.map((item) => {
            if (item.victorioso) {
                victorias++;
            } else {
                derrotas++;
            }
            return {
                fecha: new Date(item.fecha),
                victorias,
                derrotas
            };
        });
    };

    const datosProgresion = parsearDatosDeProgresion(estadisticasAvanzadas);


    const graficoProgresion = {
        chart: {
            type: 'line',
            backgroundColor: 'rgba(0, 0, 0, 0)',
        },
        title: {
            text: 'Progresión de Victorias y Derrotas',
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
        series: [
            {
                name: 'Victorias',
                data: datosProgresion.map(item => [item.fecha.getTime(), item.victorias]),
                color: '#4caf50'
            },
            {
                name: 'Derrotas',
                data: datosProgresion.map(item => [item.fecha.getTime(), item.derrotas]),
                color: '#f44336'
            }
        ]
    };

    return (
        <HighchartsReact
            highcharts={Highcharts}
            options={graficoProgresion}
        />
    );
};

export default GraficoProgresion;
