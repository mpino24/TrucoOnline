import React, { useState, useEffect } from 'react';
import Highcharts from 'highcharts';
import HighchartsReact from 'highcharts-react-official';
import useFetchState from '../util/useFetchState.js';

const RankingGlobal = ({ jwt, setMessage, setVisible, setMostrarRanking }) => {
    const [ranking, setRanking] = useFetchState([], "/api/v1/estadisticas/ranking", jwt, setMessage, setVisible);

    const rankingOrdenado = ranking.sort((a, b) => b.victorias - a.victorias);
    
    const maxVictorias = rankingOrdenado[0]?.victorias; 

    const options = {
        chart: {
            type: 'bar',
            backgroundColor: 'transparent',
            height: '100%',
        },
        title: {
            text: 'Ranking Global',
            style: {
                color: 'white',
                fontSize: '30px',
            },
        },
        xAxis: {
            categories: rankingOrdenado.map(item => item.username),
            title: {
                text: '',
                style: {
                    color: 'white',
                    fontSize: '20px',
                },
            },
            labels: {
                style: {
                    color: 'white',
                    fontSize:'20px'
                },
            },
            reversed: true,
        },
        yAxis: {
            min: 0,
            title: {
                text: 'Victorias',
                align: 'high',
                style: {
                    color: 'white',
                    fontSize: '16px',
                },
            },
            labels: {
                style: {
                    color: 'white',
                },
            },
        },
        series: [{
            name: 'Victorias',
            data: rankingOrdenado.map(item => item.victorias),
            color: '#00BFFF',
            dataLabels: {
                enabled: true,
                style: {
                    color: 'white',
                },
                formatter: function() {
                    if (this.y === maxVictorias) {
                        return `${this.y} ⭐`; 
                    }
                    return this.y;
                }
            },
        }],
        plotOptions: {
            bar: {
                dataLabels: {
                    enabled: true,
                    style: {
                        color: 'white',
                    },
                },
            },
        },
        credits: {
            enabled: false,
        },
    };

    return (
        <div style={{ display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', height: '100vh', backgroundColor: 'rgb(0,0,0,0)', color: 'white', padding: '20px', }}>
            {ranking.length > 0 ? (
                <div style={{ width: '75vh'}}> 
                    <HighchartsReact
                        highcharts={Highcharts}
                        options={options}
                    />
                </div>
            ) : (
                <p>Cargando el ranking...</p>
            )}
            <button 
                onClick={() => setMostrarRanking(false)}
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
            >Volver</button>
        </div>
    );
};

export default RankingGlobal;
