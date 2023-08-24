document.addEventListener("DOMContentLoaded", () => {
    const weatherDataElement = document.getElementById("weatherData");

    // Replace with your API endpoint URL
    const apiUrl = "/weather/{country}/{city}";

    // Replace with actual country and city
    const country = "pl";
    const city = "warsaw";

    fetch(apiUrl.replace("{country}", country).replace("{city}", city))
        .then(response => response.json())
        .then(data => {
            const weatherInfo = `
                <h2>${data.city}</h2>
                <p>Temperature: ${data.temperature} °C</p>
                <p>Precipitation: ${data.precipitation} mm</p>
                <p>Pressure: ${data.pressure} hPa</p>
                <p>Wind Speed: ${data.windSpeed} km/h</p>
            `;
            weatherDataElement.innerHTML = weatherInfo;
        })
        .catch(error => {
            console.error("Error fetching weather data:", error);
            weatherDataElement.innerHTML = "Error fetching weather data";
        });
});