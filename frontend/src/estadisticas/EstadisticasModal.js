import { forwardRef,  useState } from 'react';
import useFetchState from '../util/useFetchState.js';
import { IoCloseCircle } from "react-icons/io5";
import tokenService from '../services/token.service.js';
import { calcularTiempo } from './calcularTiempo.js';
import RenderizarLogros from './RenderizarLogros.js';
import GraficoProgresion from './GraficoProgresion.js';
import GraficoCorrelacion from './GraficoCorrelacion.js';
import GraficoBurbujas from './GraficoBurbujas.js';
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
            overflowY: 'auto',
                scrollbarWidth: 'none',
        msOverflowStyle: 'none',
        flexWrap: 'wrap'
        }}>
            {console.log(estadisticasAvanzadas)}  {/* Para debuggear */}
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
                <div style={{ display: 'flex', width: '80%', height: '80%', flexDirection: 'row', alignItems: 'center', justifyContent: 'center'}}>
                    <div style={{ display: 'flex', width: '80%', height: '80%', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', padding: '20px' }}>
                        <h2 style={{ color: 'white', marginBottom: '20px' }}>Estadísticas 📊</h2>
                        <p style={{ fontSize: "18px" }}>{calcularTiempo(estadisticas?.tiempoJugado, 0)} de tiempo jugado</p>{/* En cero para tener el tiempo total */}
                        <div style={{ width: '90%', maxWidth: '600px' }}>
                             <GraficoPartidas estadisticas={estadisticas}/>

                            <BotonDeCambio
                                    color="#9c27b0"
                                    textoMostrar="Ver comparativa global"
                                    textoOcultar="Ocultar comparativa global"
                                >
                                    <GraficoGlobalAraña estadisticas={estadisticas} jwt={jwt} setMessage={setMessage} setVisible={setVisible} />
                                </BotonDeCambio>


                        </div>
                         
                        
                        <div style={{ display: 'flex', flexDirection: 'row' }}>
                            <div style={{ display: 'flex', flexDirection: 'column' }}> {/*GRAFICO PROGRESION */}
                                <BotonDeCambio
                                    color="#ff5722"
                                    textoMostrar="Mostrar Progresión"
                                    textoOcultar="Ocultar Progresión"
                                >
                                    <GraficoProgresion estadisticasAvanzadas={estadisticasAvanzadas} />
                                </BotonDeCambio>
                            </div>
                            <div style={{ display: 'flex', flexDirection: 'column' }}> {/*GRAFICO CORRELACION */}
                            <BotonDeCambio
                                    color="#ff5722"
                                    textoMostrar="Mostrar correlación intentos de engaño y resultado"
                                    textoOcultar="Ocultar correlación intentos de engaño y resultado"
                                >
                                    <GraficoCorrelacion estadisticasAvanzadas={estadisticasAvanzadas} />
                                </BotonDeCambio>
                            </div>
                            <div style={{ display: 'flex', flexDirection: 'column' }}> {/*GRAFICO BURBUJAS */}
                            <BotonDeCambio
                                    color="#ff5722"
                                    textoMostrar="Mostrar relación de flores/victorias"
                                    textoOcultar="Ocultar relación de flores/victorias"
                                >
                                    <GraficoBurbujas estadisticasAvanzadas={estadisticasAvanzadas} estadisticas={estadisticas} />
                                </BotonDeCambio>
                            
        
                            </div>

                        </div>
                    </div>
                    
                    <RenderizarLogros jwt={jwt} setMessage={setMessage} setVisible={setVisible} />
                    
                </div>
            </div>
        </div>
    );
});

export default EstadisticasModal;