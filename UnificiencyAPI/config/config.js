
//mysql settings
exports.HOST = 'localhost';
exports.MAX_CONNECTIONS = 100;
exports.USER = 'root';
exports.PASSWORD = 'root';
exports.DATABASE = 'msp';


/*
//mysql settings
exports.HOST = 'romue404.mysql.pythonanywhere-services.com';
exports.MAX_CONNECTIONS = 100;
exports.USER = 'romue404';
exports.PASSWORD = 'neuralnetwork';
exports.DATABASE = 'romue404$unificency';
*/

//google api settings

var apiKeys = [
  'AIzaSyCsy3MraPGqNfaEx_prZa7n0yo0dNsIEE4',
  'AIzaSyDksQ6dtfrir8R3ycnzGk42qMKaXfLMjAo',
  'AIzaSyC_bpIy6SCY7udJOupuj87Vlg9tkFhvmrY'
];

exports.GOOGLE_API_KEY = apiKeys[0];
exports.DistanceMatrixAPI = "https://maps.googleapis.com/maps/api/distancematrix/json";

// jwt secret
exports.secret = 'p9Bv<3Eid9%$i01';

exports.pythonAPI = 'http://romue404.pythonanywhere.com/api/';

exports.firebaseAPI = 'https://fcm.googleapis.com/fcm/send';

exports.firebaseAPIKey = {
  "Content-Type": "application/json",
  "Authorization": "key=AAAAntf0k9g:APA91bHcmbj33OnjcIDGLUpTYX_RJ9oq45AQQn8KUsLCv3mdM4tp3yYHVVS1ZsKGmRTjMEkmN_x1SjEJ0SXrtqCy1Lkb0oCd0aI-qapW7TCTVos_STk0MyPRWlQIA-8Wc0CPdY0ghGMA"
}





