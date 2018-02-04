const startButton = document.getElementById('start-button');
const video = document.querySelector('video');

startButton.addEventListener('click', () => {
    console.log('Entered');
    if (video.paused) {
        video.play();
        startButton.textContent = 'Остановить';
    } else {
        video.pause();
        startButton.textContent = 'Генерировать';
    }
});