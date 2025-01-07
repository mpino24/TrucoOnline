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
import RenderizarLogros from './RenderizarLogros.js';
import GraficoProgresion from './GraficoProgresion.js';
import GraficoCorrelacion from './GraficoCorrelacion.js';
import GraficoColumnas from './GraficoColumnas.js';
import BotonDeCambio from './BotonDeCambio';
import GraficoGlobalAraña from './GraficoGlobalAraña.js';
import GraficoPartidas from './GraficoPartidas.js';

const jwt = tokenService.getLocalAccessToken();

const EstadisticasModal = forwardRef((props, ref) => {
    const closeModal = () => props.setModalVisible(false);
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [estadisticas, setEstadisticas] = useFetchState({}, '/api/v1/estadisticas/misEstadisticas', jwt, setMessage, setVisible);
    const [estadisticasAvanzadas, setEstadisticasAvanzadas] = useFetchState([], '/api/v1/estadisticas/misEstadisticas/datosPorPartida', jwt, setMessage, setVisible);

    return (
        <div style={{
            position: 'absolute',
            top: 0,
            left: 0,
            right: 0,
            width: '100%',
            height: '100%',
            backgroundColor: 'rgba(0, 0, 0, 0.38)',
            display: 'flex',
            flexDirection: 'row',
            justifyContent: 'space-between',
            alignItems: 'stretch', // Cambiado a 'stretch' para que los contenedores de logros y estadisticas ocupen la misma altura
            zIndex: 1000,
            overflowY: 'auto',
            scrollbarWidth: 'none',
            msOverflowStyle: 'none',
            padding: '20px', // Añadido un pequeño espacio alrededor
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
                    zIndex: 1000000000
                }}
                onClick={closeModal}
            />
            {console.log(estadisticasAvanzadas)}  {/* Para debuggear */}



            <div style={{
                display: 'flex',
                flexDirection: 'column',
                flexGrow: 1,
                alignItems: 'center',
                justifyContent: 'center',
                flex: 1, // Ambos ocupan la misma cantidad de espacio
                padding: '20px',
                overflowY: 'auto', // Añadido para que si hay muchos gráficos, pueda desplazarse
                height: '100%',
                width:'100%',
                backgroundColor: 'rgba(0, 0, 0, 0.3)',
                borderRadius: '10px', // Un pequeño borde redondeado para los contenedores
                scrollbarWidth: 'none',
                msOverflowStyle: 'none'
            }}>
                <h2 style={{ color: 'white', marginBottom: '20px' }}>Estadísticas 📊</h2>
                <p style={{ fontSize: "18px", color: 'white' }}>{calcularTiempo(estadisticas?.tiempoJugado, 0)} de tiempo jugado</p>

                <GraficoPartidas estadisticas={estadisticas} />
                <BotonDeCambio
                    color="#9c27b0"
                    textoMostrar="Ver comparativa global"
                    textoOcultar="Ocultar comparativa global"
                >
                    <GraficoGlobalAraña estadisticas={estadisticas} jwt={jwt} setMessage={setMessage} setVisible={setVisible} />
                </BotonDeCambio>


                <BotonDeCambio
                    color="#b22222"
                    textoMostrar="Mostrar Progresión"
                    textoOcultar="Ocultar Progresión"
                >
                    <GraficoProgresion estadisticasAvanzadas={estadisticasAvanzadas} />
                </BotonDeCambio>

                <BotonDeCambio
                    color="#4682b4"
                    textoMostrar="Mostrar correlación intentos de engaño y resultado"
                    textoOcultar="Ocultar correlación intentos de engaño y resultado"
                >
                    <GraficoCorrelacion estadisticasAvanzadas={estadisticasAvanzadas} />
                </BotonDeCambio>

                <BotonDeCambio
                    color="#d2691e"
                    textoMostrar="Mostrar relación de flores/victorias"
                    textoOcultar="Ocultar relación de flores/victorias"
                >
                    <GraficoColumnas estadisticasAvanzadas={estadisticasAvanzadas} estadisticas={estadisticas} />
                </BotonDeCambio>

            </div>

            <div style={{
                display: 'flex',
                flexDirection: 'column',
                flex: 1,
                alignItems: 'center',
                justifyContent: 'center',
                padding: '20px',
                overflowY: 'auto',
                height: '100%',
                backgroundColor: 'rgba(0, 0, 0, 0.3)',
                borderRadius: '10px',
            }}>
                <RenderizarLogros jwt={jwt} setMessage={setMessage} setVisible={setVisible} />
            </div>


        </div>
    );
});

export default EstadisticasModal;