import { forwardRef, useState } from 'react';
import useFetchState from '../util/useFetchState.js';

import { IoCloseCircle } from "react-icons/io5";

import tokenService from '../services/token.service.js';

import { calcularTiempo } from './calcularTiempo.js';
import RenderizarLogros from './RenderizarLogros.js';
import GraficoProgresion from './GraficoProgresion.js';
import GraficoCorrelacion from './GraficoCorrelacion.js';
import GraficoColumnas from './GraficoColumnas.js';
import BotonDeCambio from './BotonDeCambio';
import GraficoGlobalAraña from './GraficoGlobalAraña.js';
import GraficoPartidas from './GraficoPartidas.js';
import { FaRankingStar } from "react-icons/fa6";
import { FaTableList } from "react-icons/fa6";
import { FaHistory } from "react-icons/fa";
import RankingGlobal from './RankingGlobal.js'
import TablaEstadisticasCompleta from './TablaEstadisticasCompletas.js'
import { useNavigate } from 'react-router-dom';

const jwt = tokenService.getLocalAccessToken();

const EstadisticasModal = forwardRef((props, ref) => {
    const closeModal = () => {
        props.setModalVisible(false)
        setMostrarRanking(false)
        setMostrarTablaEstadisticas(false)
    };
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [estadisticas, setEstadisticas] = useFetchState({}, '/api/v1/estadisticas/misEstadisticas', jwt, setMessage, setVisible);
    const [estadisticasAvanzadas, setEstadisticasAvanzadas] = useFetchState([], '/api/v1/estadisticas/misEstadisticas/datosPorPartida', jwt, setMessage, setVisible);
    const navigate = useNavigate();

    const [mostrarRanking, setMostrarRanking] = useState(false)

    const [mostrarTablaEstadisticas, setMostrarTablaEstadisticas] = useState(false)


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
            alignItems: 'stretch',
            zIndex: 1000,
            overflowY: 'auto',
            scrollbarWidth: 'none',
            msOverflowStyle: 'none',
            padding: '20px',
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


            {!mostrarRanking && !mostrarTablaEstadisticas && <>
                <div style={{
                    display: 'flex',
                    flexDirection: 'column',
                    flex: 1,
                    alignItems: 'center',
                    justifyContent: 'flex-start',
                    padding: '20px',
                    overflowY: 'auto',
                    maxHeight: '100%',
                    backgroundColor: 'rgba(0, 0, 0, 0.3)',
                    borderRadius: '10px',
                    width: '100%',
                    scrollbarWidth: 'none',
                    msOverflowStyle: 'none'
                }}>
                    <h2 style={{ color: 'white', marginBottom: '20px' }}>Estadísticas 📊</h2>
                    <p style={{ fontSize: "18px", color: 'white' }}>{calcularTiempo(estadisticas?.tiempoJugado, 0)} de tiempo jugado ⌛</p>

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


                <FaRankingStar style={{
                    position: 'fixed',
                    bottom: 0,
                    left: 5,
                    margin: '20px',
                    zIndex: 9999, scale: '3',


                }} onMouseEnter={(e) => {
                    e.target.style.color = 'rgb(255, 255,255,0.8)';
                    e.target.style.transform = 'scale(1.05)';
                }}
                    onMouseLeave={(e) => {
                        e.target.style.color = 'white';
                        e.target.style.transform = 'scale(1)';
                    }} color='white' onClick={() => setMostrarRanking(true)} />


                <FaTableList style={{
                    position: 'fixed',
                    bottom: 0,
                    left: 65,
                    margin: '20px',
                    zIndex: 9999, scale: '3',


                }} onMouseEnter={(e) => {
                    e.target.style.color = 'rgb(255, 255,255,0.8)';
                    e.target.style.transform = 'scale(1.05)';
                }}
                    onMouseLeave={(e) => {
                        e.target.style.color = 'white';
                        e.target.style.transform = 'scale(1)';
                    }} color='white' onClick={() => setMostrarTablaEstadisticas(true)} />

                <FaHistory style={{
                    position: 'fixed',
                    bottom: 0,
                    right: 5,
                    margin: '20px',
                    zIndex: 9999, scale: '3',


                }} onMouseEnter={(e) => {
                    e.target.style.color = 'rgb(255, 255,255,0.8)';
                    e.target.style.transform = 'scale(1.05)';
                }}
                    onMouseLeave={(e) => {
                        e.target.style.color = 'white';
                        e.target.style.transform = 'scale(1)';
                    }} color='white' onClick={() => {
                        navigate("/historial")
                        closeModal()
                    }} />
            </>}

            {mostrarRanking && (
                <div style={{ position: 'fixed', width: "100%", height: "100%" }}>
                    <RankingGlobal jwt={jwt} setMessage={setMessage} setVisible={setVisible} setMostrarRanking={setMostrarRanking} />
                </div>)}
            {mostrarTablaEstadisticas && (

                <div style={{
                    position: 'fixed',
                    top: 0,
                    left: 0,
                    width: '100%',
                    height: '100%',
                    backgroundColor: 'rgba(0, 0, 0, 0.8)',
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center',
                    zIndex: 1000,
                    overflowY: 'auto'
                }}>
                    <TablaEstadisticasCompleta jwt={jwt} setMessage={setMessage} setVisible={setVisible} setMostrarTablaEstadisticas={setMostrarTablaEstadisticas} estadisticas={estadisticas} />
                </div>)}

        </div>

    );
});

export default EstadisticasModal;