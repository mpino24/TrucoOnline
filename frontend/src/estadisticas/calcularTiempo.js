export function calcularTiempo(tiemposEnSegundos, partidasTotales) {
    let promedio = partidasTotales !== 0 ? tiemposEnSegundos / partidasTotales : tiemposEnSegundos;
    if (isNaN(promedio)) {
        promedio = tiemposEnSegundos;
    }
    let horas = Math.floor(promedio / 3600);
    let minutos = Math.floor((promedio % 3600) / 60);
    let segundos = Math.floor(promedio % 60);

    let dias = Math.floor(horas / 24);
    horas = horas % 24;
    let años = Math.floor(dias / 365);
    dias = dias % 365;
    let tiempoFormateado = '';
    
    if (años > 0) {
        tiempoFormateado += `${años} años`;
    }
    if (dias > 0) {
        tiempoFormateado += `${tiempoFormateado ? ', ' : ''}${dias} días`;
    }
    if (horas > 0) {
        tiempoFormateado += `${tiempoFormateado ? ', ' : ''}${horas} horas`;
    }
    if (minutos > 0) {
        tiempoFormateado += `${tiempoFormateado ? ', ' : ''}${minutos < 10 ? '0' + minutos : minutos} minutos`;
    }
    if (segundos > 0 || tiempoFormateado === '') {
        tiempoFormateado += `${tiempoFormateado ? ' y ' : ''}${segundos < 10 ? '0' + segundos : segundos} segundos`;
    }
    
    return tiempoFormateado;
}
