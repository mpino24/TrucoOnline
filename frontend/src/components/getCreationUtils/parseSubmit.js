
import generateCode from './generateCode.js'
export default function parseSubmit(partida, partidaParseada){

        
   if(partida.conFlor){
      partidaParseada.conFlor=true
  }
  if(partida.puntosMaximos){
      partidaParseada.puntosMaximos=30
  }
  if(partida.visibilidad){
      partidaParseada.visibilidad='PRIVADA'
  }
      partidaParseada.numJugadores=partida.numJugadores
      partidaParseada.codigo=generateCode()
    return partidaParseada;

}
