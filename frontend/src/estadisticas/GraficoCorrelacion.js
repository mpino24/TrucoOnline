import React from 'react';
import HighchartsReact from 'highcharts-react-official';
import Highcharts from 'highcharts';

const GraficoCorrelacion = ({ estadisticasAvanzadas }) => { 
    const parsearDatosDeCorrelacion = (data) => {
        let victoriasMap = {};
        let derrotasMap = {};

        data.forEach(item => {
            const fecha = new Date(item.fecha).getTime();

            if (item.victorioso) {
                if (!victoriasMap[fecha]) {
                    victoriasMap[fecha] = 0;
                }
                victoriasMap[fecha] += item.enganos;
            } else {
                if (!derrotasMap[fecha]) {
                    derrotasMap[fecha] = 0;
                }
                derrotasMap[fecha] += item.atrapados;
            }
        });

        return { victoriasMap, derrotasMap };
    };

    const { victoriasMap, derrotasMap } = parsearDatosDeCorrelacion(estadisticasAvanzadas);

    // Calculamos los totales
    const totalVictorias = Object.keys(victoriasMap).length;
    const totalEnganos = Object.values(victoriasMap).reduce((acc, val) => acc + val, 0);
    const totalDerrotas = Object.keys(derrotasMap).length;
    const totalAtrapados = Object.values(derrotasMap).reduce((acc, val) => acc + val, 0);

    const graficoCorrelacion = {
        chart: {
            type: 'bubble',
            backgroundColor: 'rgba(0, 0, 0, 0)',
            zoomType: 'xy',
        },
        title: {
            text: `Correlación: Victorias y Derrotas`,
            style: { color: '#ffffff' },
        },
        subtitle: {
            text: `Victorias: ${totalVictorias}, Engaños Totales: ${totalEnganos}<br>Derrotas: ${totalDerrotas}, Atrapados Totales: ${totalAtrapados}`,
            style: { color: '#ffffff' },
        },
        xAxis: {
            type: 'datetime',
            title: {
                text: 'Fecha y Hora',
                style: { color: '#ffffff' },
            },
            labels: {
                style: { color: '#ffffff' },
            },
        },
        yAxis: {
            title: {
                text: 'Cantidad',
                style: { color: '#ffffff' },
            },
            labels: {
                style: { color: '#ffffff' },
            },
        },
        tooltip: {
            useHTML: true,
            formatter: function () {
                const tipo = this.series.name.includes('Victorias') ? 'Victoria' : 'Derrota';
                const total = this.series.name.includes('Victorias') 
                    ? `Engaños hechos: ${this.point.z}` 
                    : `Veces atrapado: ${this.point.z}`;
                return `
                    <b>${tipo}</b><br>
                    Fecha: ${Highcharts.dateFormat('%e %b %Y, %H:%M', this.x)}<br>
                    ${total}
                `;
            },
        },
        series: [
            {
                name: 'Victorias con Engaños',
                data: Object.keys(victoriasMap).map(fecha => ({
                    x: parseInt(fecha), // fecha en formato timestamp
                    y: victoriasMap[fecha], // cantidad de victorias
                    z: victoriasMap[fecha], // tamaño del marcador según cantidad de engaños
                })),
                color: '#4caf50',
                marker: {
                    symbol: 'circle',
                    radius: 5,
                },
            },
            {
                name: 'Derrotas siendo atrapado',
                data: Object.keys(derrotasMap).map(fecha => ({
                    x: parseInt(fecha), // fecha en formato timestamp
                    y: derrotasMap[fecha], // cantidad de derrotas
                    z: derrotasMap[fecha], // tamaño del marcador según cantidad de atrapados
                })),
                color: '#f44336',
                marker: {
                    symbol: 'triangle',
                    radius: 5,
                },
            },
        ],
        plotOptions: {
            bubble: {
                minSize: 5,
                maxSize: 20,
            },
        },
    };

    return (
        <HighchartsReact
            highcharts={Highcharts}
            options={graficoCorrelacion}
        />
    );
};

export default GraficoCorrelacion;
