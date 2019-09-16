const R = 6371; // Radius of the earth in km

function deg2rad(deg) {
  return deg * (Math.PI / 180)
}

/**
 *
 * @param lat1
 * @param lon1
 * @param lat2
 * @param lon2
 * @returns {number}, in KM
 */
function distance(lat1, lon1, lat2, lon2) {
  const dLat = deg2rad(lat2 - lat1);  // deg2rad below
  const dLon = deg2rad(lon2 - lon1);
  const a =
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
    Math.sin(dLon / 2) * Math.sin(dLon / 2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  return R * c;
}

function dist2Time() {
  const min = distance / 0.070
  return `${min} min`
}

export default {
  distance,
  deg2rad,
  dist2Time
}
