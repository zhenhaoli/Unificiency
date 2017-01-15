
//mysql settings
exports.HOST = 'localhost';
exports.MAX_CONNECTIONS = 100;
exports.USER = 'root';
exports.PASSWORD = 'root';
exports.DATABASE = 'msp';

//google api settings

var apiKeys = [
  'AIzaSyCsy3MraPGqNfaEx_prZa7n0yo0dNsIEE4',
  'AIzaSyDksQ6dtfrir8R3ycnzGk42qMKaXfLMjAo',
  'AIzaSyC_bpIy6SCY7udJOupuj87Vlg9tkFhvmrY'
];

exports.GOOGLE_API_KEY = apiKeys[0];

exports.DistanceMatrixAPI = "https://maps.googleapis.com/maps/api/distancematrix/json";

