import { forwardRef, useEffect, useState } from 'react';
import useFetchState from '../util/useFetchState.js';
import Highcharts from 'highcharts';
import HighchartsReact from 'highcharts-react-official';
import { IoCloseCircle } from "react-icons/io5";
import jwt_decode from "jwt-decode";
import tokenService from '../services/token.service.js';
import HighchartsMore from 'highcharts/highcharts-more';
import LogroComponent from './LogroComponent.js';
import { calcularTiempo } from './calcularTiempo.js';

const jwt = tokenService.getLocalAccessToken();

const EstadisticasModal = forwardRef((props, ref) => {
    const closeModal = () => props.setModalVisible(false);
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [graficoComparativo, setGraficoComparativo] = useState(false);
    const [estadisticas, setEstadisticas] = useFetchState({}, '/api/v1/estadisticas/misEstadisticas', jwt, setMessage, setVisible);
    const [estadisticasGlobales, setEstadisticasGlobales] = useFetchState({}, '/api/v1/estadisticas/estadisticasGlobales', jwt, setMessage, setVisible);
    const [graficoActualPartidas, setGraficoActualPartidas] = useState("resultados");
    const [logrosMios, setLogrosMios] = useState(true);
    const [listaMisLogros, setListaMisLogros] = useFetchState([], '/api/v1/logros/misLogros', jwt, setMessage, setVisible);
    const [listaLogrosGlobales, setListaLogrosGlobales] = useFetchState([], '/api/v1/logros', jwt, setMessage, setVisible);
    const [totalLogros, setTotalLogros] = useFetchState(0, '/api/v1/logros/total', jwt, setMessage, setVisible);
    const [estadisticasAvanzadas, setEstadisticasAvanzadas] = useFetchState([], '/api/v1/estadisticas/misEstadisticas/datosPorPartida', jwt, setMessage, setVisible);
    const [mostrarProgresion, setMostrarProgresion] = useState(false)
    const [mostrarCorrelacion,setMostrarCorrelacion] =useState(false)
    const [mostrarBurbujas,setMostrarBurbujas] =useState(false)

    const [roles, setRoles] = useState([]);

    useEffect(() => {
        if (jwt) {
            setRoles(jwt_decode(jwt).authorities);
        }
    }, [jwt]);

    function cambiarLogros() {
        setLogrosMios(!logrosMios);
    }

    //LOGROS
    const renderLogros = () => {
        let logros;
        const soyAdmin = roles.includes('ADMIN');
        if (logrosMios) {
            logros = listaMisLogros;
        } else {
            logros = listaLogrosGlobales;
        }

        const logrosRestantes = totalLogros - listaMisLogros.length;
        let textoLogrosRestantes = `Todavia te quedan ${logrosRestantes} logros por conseguir`;
        if (logrosRestantes === 1) {
            textoLogrosRestantes = 'Solamente te falta un logro!!!';
        }

        if (logros && logros.length > 0) {
            return (
                <>
                    <h3 style={{ color: 'white', marginBottom: '20px' }}>
                        {logrosMios ? `${listaMisLogros?.length} Logros obtenidos` : soyAdmin ? "Logros globales" : textoLogrosRestantes}
                    </h3>
                    <div style={logrosGridStyle}>
                        {logros.map((logro, index) => (
                            <div key={index} style={logroCardStyle}>
                                <LogroComponent logro={logro} />
                            </div>
                        ))}
                    </div>
                </>
            );
        } else {
            return (
                <>
                    {logrosMios && (
                        <div>
                            <h3 style={{ color: 'white' }}>No tenés ningún logro todavía</h3>
                            <p style={{ color: 'white' }}>Seguí jugando para desbloquear logros.</p>
                        </div>
                    )}
                    {!logrosMios && (
                        <div>
                            <h3 style={{ color: 'white' }}>
                                {listaMisLogros.length === totalLogros ? "Tenes todos los logros!" : textoLogrosRestantes}
                            </h3>
                            <p style={{ color: 'white' }}>
                                {listaMisLogros.length === totalLogros ? "Realmente sos el amo del Truco" : "Mucha suerte!"}
                            </p>
                        </div>
                    )}
                </>
            );
        }
    };

    const logrosGridStyle = {
        display: 'grid',
        gridTemplateColumns: 'repeat(2, 1fr)',
        gap: '20px',
        maxHeight: '600px',
        overflowY: 'auto',
        padding: '20px',
        backgroundColor: 'rgba(255, 255, 255, 0)',
        borderRadius: '10px',
        scrollbarWidth: 'none',
        msOverflowStyle: 'none',
    };

    const logroCardStyle = {
        border: '1px solid #ddd',
        borderRadius: '10px',
        padding: '10px',
        backgroundColor: 'rgba(0, 0, 0, 0.2)',
        boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
        textAlign: 'center',
        position: 'relative',
    };

    // ESTADISTICAS:
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

    function cambiarGrafico() {
        setGraficoActualPartidas(graficoActualPartidas === "resultados" ? "tiposPartidas" : "resultados");
    }

    function cambiarAGraficoComparativo() {
        setGraficoComparativo(!graficoComparativo);
    }

    function cambiarGraficoDeProgresion() {
        setMostrarProgresion(!mostrarProgresion);
    }
    function cambiarGraficoDeCorrelacion() {
        setMostrarCorrelacion(!mostrarCorrelacion);
    }
    function cambiarGraficoDeBurbujas() {
        setMostrarBurbujas(!mostrarBurbujas);
    }


    //PROGRESION VICTORIAS DERROTAS EN BASE AL TIEMPO
    const parsearDatosDeProgresion = (data) => {
        let victories = 0;
        let defeats = 0;
        return data.map((item) => {
            if (item.victorioso) {
                victories++;
            } else {
                defeats++;
            }
            return {
                fecha: new Date(item.fecha), 
                victories,
                defeats
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
                data: datosProgresion.map(item => [item.fecha.getTime(), item.victories]),
                color: '#4caf50'
            },
            {
                name: 'Derrotas',
                data: datosProgresion.map(item => [item.fecha.getTime(), item.defeats]),
                color: '#f44336'
            }
        ]
    };


    //Correlacion victorias con engaños y derrotas con atrapados
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


    const datosBurbujas = [
        {
            x: estadisticas.partidasA2, // Eje X: Partidas jugadas a 2 jugadores
            y: estadisticas.partidasConFlor / estadisticas.partidasJugadas, // Eje Y: Proporción de partidas con flor
            z: estadisticas.numeroFlores, // Tamaño: Cantidad de flores cantadas
            name: 'Partidas a 2 jugadores'
        },
        {
            x: estadisticas.partidasA4, // Eje X: Partidas jugadas a 4 jugadores
            y: estadisticas.partidasConFlor / estadisticas.partidasJugadas, // Eje Y: Proporción de partidas con flor
            z: estadisticas.numeroFlores , // Tamaño: Cantidad de flores cantadas dividida por 2
            name: 'Partidas a 4 jugadores'
        },
        {
            x: estadisticas.partidasA6, // Eje X: Partidas jugadas a 6 jugadores
            y: estadisticas.partidasConFlor / estadisticas.partidasJugadas, // Eje Y: Proporción de partidas con flor
            z: estadisticas.numeroFlores , // Tamaño: Cantidad de flores cantadas dividida por 3
            name: 'Partidas a 6 jugadores'
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
            text: 'Relación entre Partidas con Flor y Flores Cantadas',
            style: { color: '#ffffff' }
        },
        xAxis: {
            title: {
                text: 'Partidas Jugadas por Modalidad',
                style: { color: '#ffffff' }
            },
            labels: {
                style: { color: '#ffffff' }
            }
        },
        yAxis: {
            title: {
                text: 'Proporción de Partidas con Flor',
                style: { color: '#ffffff' }
            },
            labels: {
                style: { color: '#ffffff' }
            }
        },
        tooltip: {
            pointFormat: 'Modalidad: {point.name}<br>' +
                         'Partidas Jugadas: {point.x}<br>' +
                         'Proporción Partidas con Flor: {point.y:.2f}<br>' +
                         'Flores Cantadas: {point.z}'
        },
        series: [{
            data: datosBurbujas,
            marker: {
                lineColor: '#ffffff'
            }
        }]
    };
    

    
    



    const globalModoAraña = {
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
                'Partidas a 4', 'Partidas a 6', 'Tiempo jugado'
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
        <div style={{
            position: 'fixed',
            top: 0,
            left: 0,
            width: '100%',
            height: '100%',
            backgroundColor: 'rgba(0, 0, 0, 0.38)',
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'space-between',
            alignItems: 'stretch',
            zIndex: 1000,
        }}>
            {console.log(estadisticasAvanzadas)}  {/* Para debuggear */}
            {console.log(listaMisLogros)}
            <div style={{
                position: 'relative',
                flex: 1,
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                color: 'white',
                fontSize: '24px',
            }}>
                <IoCloseCircle
                    style={{
                        position: 'absolute',
                        top: 10,
                        right: 10,
                        width: 30,
                        height: 30,
                        cursor: 'pointer',
                        color: 'white',
                    }}
                    onClick={closeModal}
                />
                <div style={{ display: 'flex', width: '80%', height: '80%', flexDirection: 'row', alignItems: 'center', justifyContent: 'center' }}>
                    <div style={{ display: 'flex', width: '80%', height: '80%', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', padding: '20px' }}>
                        <h2 style={{ color: 'white', marginBottom: '20px' }}>Estadísticas 📊</h2>
                        <p style={{ fontSize: "18px" }}>{calcularTiempo(estadisticas?.tiempoJugado, 0)} de tiempo jugado</p>{/* En cero para tener el tiempo total */}
                        <div style={{ width: '90%', maxWidth: '600px' }}>
                            {!graficoComparativo && <HighchartsReact
                                highcharts={Highcharts}
                                options={graficoActualPartidas === "resultados" ? victoriasDerrotas : tiposPartidas}
                            />}
                            {graficoComparativo && <HighchartsReact
                                highcharts={Highcharts}
                                options={globalModoAraña}
                            />}
                            <div style={{display:'flex', flexDirection:'row'}}>
                            {mostrarProgresion && <HighchartsReact
                                highcharts= {Highcharts}
                            options={graficoProgresion}/>}
                            {mostrarCorrelacion && <HighchartsReact
                                highcharts= {Highcharts}
                                options={graficoCorrelacion}/>}
                            {mostrarCorrelacion && <HighchartsReact
                                highcharts= {Highcharts}
                                options={graficoCorrelacion}/>}
                            {mostrarBurbujas && <HighchartsReact
                                highcharts= {Highcharts}
                                options={graficoBurbujas}/>}
                            </div>
                            
                        </div>
                        {!graficoComparativo && <button
                            onClick={() => cambiarGrafico()}
                            style={{
                                marginRight: '10px',
                                marginBottom: '10px',
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
                        </button>}
                        <button
                            onClick={() => cambiarAGraficoComparativo()}
                            style={{
                                padding: '10px 20px',
                                backgroundColor: '#9c27b0',
                                color: 'white',
                                border: 'none',
                                borderRadius: '25px',
                                cursor: 'pointer',
                                transition: 'background-color 0.3s, transform 0.3s',
                                fontSize: '16px',
                                boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
                            }}
                            onMouseEnter={(e) => {
                                e.target.style.backgroundColor = '#7b1fa2';
                                e.target.style.transform = 'scale(1.05)';
                            }}
                            onMouseLeave={(e) => {
                                e.target.style.backgroundColor = '#9c27b0';
                                e.target.style.transform = 'scale(1)';
                            }}
                        >
                            {graficoComparativo ? "Volver" : "Ver comparativa global"}
                        </button>
                        <button
                            onClick={cambiarGraficoDeProgresion}
                            style={{
                                padding: '10px 20px',
                                backgroundColor: '#ff5722',
                                color: 'white',
                                border: 'none',
                                borderRadius: '25px',
                                cursor: 'pointer',
                                transition: 'background-color 0.3s, transform 0.3s',
                                fontSize: '16px',
                                marginTop: '10px',
                                boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
                            }}
                            onMouseEnter={(e) => {
                                e.target.style.backgroundColor = '#e64a19';
                                e.target.style.transform = 'scale(1.05)';
                            }}
                            onMouseLeave={(e) => {
                                e.target.style.backgroundColor = '#ff5722';
                                e.target.style.transform = 'scale(1)';
                            }}
                        >
                            {mostrarProgresion ? "Ocultar Progresión" : "Mostrar Progresión"}
                        </button>
                        <button
                            onClick={cambiarGraficoDeCorrelacion}
                            style={{
                                padding: '10px 20px',
                                backgroundColor: '#ff5722',
                                color: 'white',
                                border: 'none',
                                borderRadius: '25px',
                                cursor: 'pointer',
                                transition: 'background-color 0.3s, transform 0.3s',
                                fontSize: '16px',
                                marginTop: '10px',
                                boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
                            }}
                            onMouseEnter={(e) => {
                                e.target.style.backgroundColor = '#e64a19';
                                e.target.style.transform = 'scale(1.05)';
                            }}
                            onMouseLeave={(e) => {
                                e.target.style.backgroundColor = '#ff5722';
                                e.target.style.transform = 'scale(1)';
                            }}
                        >
                            {mostrarCorrelacion ? "Ocultar Correlacion" : "Mostrar Correlacion"}
                        </button>
                        <button
                            onClick={cambiarGraficoDeBurbujas}
                            style={{
                                padding: '10px 20px',
                                backgroundColor: '#ff5722',
                                color: 'white',
                                border: 'none',
                                borderRadius: '25px',
                                cursor: 'pointer',
                                transition: 'background-color 0.3s, transform 0.3s',
                                fontSize: '16px',
                                marginTop: '10px',
                                boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
                            }}
                            onMouseEnter={(e) => {
                                e.target.style.backgroundColor = '#e64a19';
                                e.target.style.transform = 'scale(1.05)';
                            }}
                            onMouseLeave={(e) => {
                                e.target.style.backgroundColor = '#ff5722';
                                e.target.style.transform = 'scale(1)';
                            }}
                        >
                            {mostrarBurbujas ? "Ocultar Relacion de Flores" : "Mostrar Relacion de Flores"}
                        </button>
                    </div>
                    <div style={{ display: 'flex', width: '100%', height: '90%', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', padding: '20px' }}>
                        {renderLogros()}
                        <button
                            onClick={() => cambiarLogros()}
                            style={{
                                padding: '10px 20px',
                                background: 'linear-gradient(45deg, #ff5722, #ff9800)',
                                color: 'white',
                                border: 'none',
                                borderRadius: '25px',
                                cursor: 'pointer',
                                transition: 'background 0.3s, transform 0.3s',
                                marginTop: '20px',
                                fontSize: '16px',
                                boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
                            }}
                            onMouseEnter={(e) => {
                                e.target.style.background = 'linear-gradient(45deg, #e64a19, #f57c00)';
                                e.target.style.transform = 'scale(1.05)';
                            }}
                            onMouseLeave={(e) => {
                                e.target.style.background = 'linear-gradient(45deg, #ff5722, #ff9800)';
                                e.target.style.transform = 'scale(1)';
                            }}
                        >
                            {logrosMios ? (roles.includes('ADMIN') ? "Ver logros globales" : "Ver logros que te faltan") : "Ver tus logros"}
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
});

export default EstadisticasModal;