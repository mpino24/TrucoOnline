export function calcularTiempo(tiemposEnSegundos, partidasTotales = 1) {
    if (isNaN(tiemposEnSegundos) || tiemposEnSegundos < 0) {
        return "0 segundos"; 
    }
    if (isNaN(partidasTotales) || partidasTotales <= 0) {
        partidasTotales = 1; 
    }
    let promedio = tiemposEnSegundos / partidasTotales;

    let horas = Math.floor(promedio / 3600);
    let minutos = Math.floor((promedio % 3600) / 60);
    let segundos = Math.floor(promedio % 60);

    let dias = Math.floor(horas / 24);
    horas = horas % 24; 
    let años = Math.floor(dias / 365);
    dias = dias % 365; 

    
    let tiempoFormateado = '';
    if (años > 0) tiempoFormateado += comprobarSingular(años, 'años');
    if (dias > 0) tiempoFormateado += `${tiempoFormateado ? ', ' : ''}${comprobarSingular(dias, "dias")}`;
    if (horas > 0) tiempoFormateado += `${tiempoFormateado ? ', ' : ''}${comprobarSingular(horas, "horas")}`;
    if (minutos > 0) tiempoFormateado += `${tiempoFormateado ? ', ' : ''}${comprobarSingular(minutos, "minutos")}`;
    if (segundos > 0 || tiempoFormateado === '') {
        tiempoFormateado += `${tiempoFormateado ? ' y ' : ''}${comprobarSingular(segundos, "segundos")}`;
    }

    return tiempoFormateado;
}


function comprobarSingular(data, texto){
    let res = data +" "+ texto
    if(data === 1){
        res = res.replace("s", "")
    }
    return res
}
