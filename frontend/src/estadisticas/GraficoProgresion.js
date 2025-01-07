import React from 'react';
import HighchartsReact from 'highcharts-react-official';
import Highcharts from 'highcharts';

const GraficoProgresion = ({ estadisticasAvanzadas }) => {
    const meses = [
        'enero', 'febrero', 'marzo', 'abril', 'mayo', 'junio',
        'julio', 'agosto', 'septiembre', 'octubre', 'noviembre', 'diciembre'
    ];


    const formatearFecha = (fecha) => {
        const date = new Date(fecha);
        const dia = date.getDate();
        const mes = meses[date.getMonth()];
        const anio = date.getFullYear();
        return `${dia} de ${mes} del ${anio}`;
    };

 
    const generarRangoDeFechas = (inicio, fin) => {
        const rango = [];
        let fechaActual = new Date(inicio);

        while (fechaActual <= fin) {
            rango.push(new Date(fechaActual));
            fechaActual.setDate(fechaActual.getDate() + 1);
        }

        return rango;
    };

    
    const parsearDatosDeProgresion = (data) => {
        const datosAgrupados = {};

        data.forEach((item) => {
            const fecha = new Date(item.fecha);
            const fechaString = fecha.toISOString().split('T')[0]; // Solo la fecha (sin hora)

            if (!datosAgrupados[fechaString]) {
                datosAgrupados[fechaString] = { victorias: 0, derrotas: 0 };
            }

            if (item.victorioso) {
                datosAgrupados[fechaString].victorias++;
            } else {
                datosAgrupados[fechaString].derrotas++;
            }
        });

        return datosAgrupados;
    };

    const datosAgrupados = parsearDatosDeProgresion(estadisticasAvanzadas);

    
    const fechas = Object.keys(datosAgrupados).map(fecha => new Date(fecha));
    const fechaInicio = new Date(Math.min(...fechas));
    const fechaFin = new Date(Math.max(...fechas));

    const rangoFechas = generarRangoDeFechas(fechaInicio, fechaFin);

    
    let acumuladoVictorias = 0;
    let acumuladoDerrotas = 0;

    const datosProgresion = rangoFechas.map(fecha => {
        const fechaString = fecha.toISOString().split('T')[0];
        const datosDia = datosAgrupados[fechaString] || { victorias: 0, derrotas: 0 };

        acumuladoVictorias += datosDia.victorias;
        acumuladoDerrotas += datosDia.derrotas;

        return {
            fecha,
            victorias: acumuladoVictorias,
            derrotas: acumuladoDerrotas,
        };
    });

    
    const datosResumidos = datosProgresion.filter((item, index, array) => {
        if (index === 0) return true; 
        const anterior = array[index - 1];
        return item.victorias !== anterior.victorias || item.derrotas !== anterior.derrotas;
    });

    const graficoProgresion = {
        chart: {
            type: 'line',
            zoomType: 'x', 
            backgroundColor: 'rgba(0, 0, 0, 0)',
        },
        title: {
            text: 'Progresión de Victorias y Derrotas',
            style: { color: '#ffffff' }
        },
        xAxis: {
            type: 'datetime', 
            title: {
                text: 'Fecha',
                style: { color: '#ffffff' }
            },
            labels: {
                formatter: function () {
                    return formatearFecha(this.value);
                },
                style: { color: '#ffffff' }
            }
        },
        yAxis: {
            title: {
                text: 'Cantidad Acumulada',
                style: { color: '#ffffff' }
            },
            labels: {
                style: { color: '#ffffff' }
            }
        },
        tooltip: {
            shared: true, 
            formatter: function () {
                const fecha = formatearFecha(this.x);
                const victorias = this.points.find(p => p.series.name === 'Victorias')?.y || 0;
                const derrotas = this.points.find(p => p.series.name === 'Derrotas')?.y || 0;
                return `<b>${fecha}</b><br>Victorias: ${victorias}<br>Derrotas: ${derrotas}`;
            }
        },
        rangeSelector: {
            enabled: true, 
            buttons: [
                { type: 'day', count: 7, text: '1 sem' },
                { type: 'month', count: 1, text: '1 mes' },
                { type: 'all', text: 'Todo' }
            ],
            selected: 2
        },
        navigator: {
            enabled: true 
        },
        series: [
            {
                name: 'Victorias',
                data: datosResumidos.map(item => [item.fecha.getTime(), item.victorias]),
                color: '#4caf50'
            },
            {
                name: 'Derrotas',
                data: datosResumidos.map(item => [item.fecha.getTime(), item.derrotas]),
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
