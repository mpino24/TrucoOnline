import { forwardRef, useEffect, useState } from 'react';
import useFetchState from '../util/useFetchState.js';
import Highcharts, { color } from 'highcharts';
import HighchartsReact from 'highcharts-react-official';
import { IoCloseCircle } from "react-icons/io5";
import jwt_decode from "jwt-decode";
import tokenService from '../services/token.service.js';
import HighchartsMore from 'highcharts/highcharts-more';
import LogroComponent from './LogroComponent.js';
const jwt = tokenService.getLocalAccessToken();
const EstadisticasModal = forwardRef((props, ref) => {
    const closeModal = () => props.setModalVisible(false);
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [graficoComparativo, setGraficoComparativo] = useState(false);
    const [estadisticas, setEstadisticas] = useFetchState({}, '/api/v1/estadisticas/misEstadisticas', jwt, setMessage, setVisible)
    const [estadisticasGlobales, setEstadisticasGlobales] = useFetchState({}, '/api/v1/estadisticas/estadisticasGlobales', jwt, setMessage, setVisible)
    const [graficoActualPartidas, setGraficoActualPartidas] = useState("resultados");
    const [logrosMios,setLogrosMios]=useState(true)
    const [listaMisLogros, setListaMisLogros] = useFetchState([], '/api/v1/logros/misLogros', jwt, setMessage, setVisible);
    const [listaLogrosGlobales, setListaLogrosGlobales] = useFetchState([], '/api/v1/logros', jwt, setMessage, setVisible);
    const [totalLogros, setTotalLogros] = useFetchState(0, '/api/v1/logros/total', jwt, setMessage, setVisible);
      const [roles, setRoles] = useState([]);
    function getRolesFromJWT(jwt) {
        return jwt_decode(jwt).authorities;
    }
    
        useEffect(() => {
            if (jwt) {
                setRoles(jwt_decode(jwt).authorities);
            }
        }, [jwt])

    function cambiarLogros(){
        setLogrosMios(logrosMios?false:true )
    }
    //LOGROS
    const renderLogros = () => {
        let logros;
        let soyAdmin = roles.includes('ADMIN')
        if(logrosMios){
            logros = listaMisLogros
        }else{
            logros = listaLogrosGlobales
        }

        if (logros && logros.length > 0) {
            return (
                <>
                    <h3 style={{ color: 'white', marginBottom: '20px' }}>{logrosMios?"Logros obtenidos": soyAdmin?"Logros globales":"Logros por conseguir"}</h3>
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
            let logrosRestantes = totalLogros-listaMisLogros.length
            let textoLogrosRestantes= `Todavia te quedan ${logrosRestantes} logros por conseguir`
            if(logrosRestantes===1){
                textoLogrosRestantes = 'Solamente te falta un logro!!!'
            }
            return (
                <>
            
                {logrosMios&& <div>
                    <h3 style={{ color: 'white' }}>No has obtenido logros todavía</h3>
                    <p style={{ color: 'white' }}>Seguí jugando para desbloquear logros.</p>
                </div>}
                {!logrosMios && <div>
                    <h3 style={{ color: 'white' }}>{listaMisLogros.length === totalLogros? "Tenes todos los logros!": textoLogrosRestantes}</h3>
                    <p style={{ color: 'white' }}>{listaMisLogros.length === totalLogros? "Realmente sos el amo del Truco": `Mucha suerte!`}</p>
                </div>}
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
                    estadisticas.partidasA4 / (estadisticas.partidasJugadas * 4) || 0, //no estoy del todo seguro, pero al ser 4 jugadores, 
                    estadisticas.partidasA6 / (estadisticas.partidasJugadas * 6) || 0, //deberia dividirse para que la media no se infle artificalmente, no?
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
    }
    function calcularTiempo(tiemposEnSegundos, partidasTotales) {
        let promedio = partidasTotales !== 0 ? tiemposEnSegundos / partidasTotales : tiemposEnSegundos;
        if (isNaN(promedio)) {
            promedio = tiemposEnSegundos;
        }
        let horas = Math.floor(promedio / 3600);
        let minutos = Math.floor((promedio % 3600) / 60);
        let segundos = Math.floor(promedio % 60);


        let tiempoFormateado = `${horas}:${minutos < 10 ? '0' + minutos : minutos}:${segundos < 10 ? '0' + segundos : segundos}`;

        return tiempoFormateado;
    }




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
            {console.log(calcularTiempo(estadisticas.tiempoJugado, estadisticas.partidasJugadas))}
            {console.log(estadisticas)}  {/* Para debuggear*/}
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
                        <h2 style={{ color: 'white', marginBottom: '20px' }}>Estadísticas</h2>
                        <div style={{ width: '90%', maxWidth: '600px' }}>
                            {!graficoComparativo && <HighchartsReact
                                highcharts={Highcharts}
                                options={graficoActualPartidas === "resultados" ? victoriasDerrotas : tiposPartidas}
                            />}
                            {graficoComparativo && <HighchartsReact
                                highcharts={Highcharts}
                                options={globalModoAraña}
                            />}
                        </div>
                        {!graficoComparativo && <button
                            onClick={() => cambiarGrafico()}
                            style={{
                                marginRight: '10px',
                                padding: '10px 20px',
                                backgroundColor: cambiarGrafico === "resultados" ? '#4caf50' : '#757575',
                                color: 'white',
                                border: 'none',
                                borderRadius: '5px',
                                cursor: 'pointer',
                            }}
                        >
                            {graficoActualPartidas === "resultados" ? "Tipos de Partidas" : "Resultados"}
                        </button>}
                        <button
                            onClick={() => cambiarAGraficoComparativo()}
                            style={{
                                padding: '10px 20px',
                                backgroundColor: graficoComparativo ? '#4caf50' : '#757575',
                                color: 'white',
                                border: 'none',
                                borderRadius: '5px',
                                cursor: 'pointer',
                            }}
                        >{graficoComparativo ? "Volver" : "Ver comparativa global"}</button>
                    </div>
                    
                        <div style={{ display: 'flex', width: '100%', height: '90%', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', padding: '20px' }}>
                            {renderLogros()}
                            {console.log(listaMisLogros?.[0]?.name)}
                            {console.log(listaMisLogros?.[0]?.descripcion)}
                            {console.log(listaMisLogros?.[0]?.imagencita)}
                            {console.log(listaMisLogros?.[0]?.metrica)}
                            {console.log(listaMisLogros?.[0]?.oculto)}
                            {console.log(listaMisLogros?.[0]?.valor)}
            
                            <button onClick={()=> cambiarLogros()}>{logrosMios? (roles.includes('ADMIN')?"Ver logros globales": "Ver logros que te faltan"): "Ver tus logros"}</button>
                        </div>
                        
             
                </div>
            </div>
        </div>
    );
});
export default EstadisticasModal;
