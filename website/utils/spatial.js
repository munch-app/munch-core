const R = 6371; // Radius of the earth in km

function deg2rad(deg) {
  return deg * (Math.PI / 180)
}

/**
 *
 * @param latLng1
 * @param latLng2
 * @returns {number}, in KM
 */
function distance(latLng1, latLng2) {
  const ll1 = latLng1.split(',')
  const ll2 = latLng2.split(',')

  const lat1 = parseFloat(ll1[0])
  const lon1 = parseFloat(ll1[1])
  const lat2 = parseFloat(ll2[0])
  const lon2 = parseFloat(ll2[1])

  const dLat = deg2rad(lat2 - lat1);  // deg2rad below
  const dLon = deg2rad(lon2 - lon1);
  const a =
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
    Math.sin(dLon / 2) * Math.sin(dLon / 2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  return R * c;
}

/**
 * @param distance in KM
 * @returns {string}
 */
function dist2Time(distance) {
  const min = distance / 0.070
  return `${min} min`
}

export default {
  distance,
  deg2rad,
  dist2Time
}
