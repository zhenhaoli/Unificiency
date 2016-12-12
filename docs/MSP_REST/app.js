var app = angular.module('RESTDOC', []);
app.controller('MainController', function($scope) {
    $scope.firstName = "John";
    $scope.lastName = "Doe";

    $scope.apis = [
        {
            "name": "GET /buildings?lat={lat}&lng={lng}",
            "description": "listet die LMU Gebäude nach der Distanz zum Benutzer"
        },
        {
            "name": "GET /buildings",
            "description": "listet alle LMU Gebäude"
        },
        {
            "name": "GET /buildings/{id}",
            "description": "zeigt ein ausgewähltes Gebäude"
        },
        {
            "name": "GET /buildings/{id}/rooms",
            "description": "zeigt alle Räume eines ausgewählten Gebäude"
        },
        {
            "name": "GET /buildings/{id}/rooms/{id}",
            "description": "zeigt Information eines ausgewählten Raums des ausgewählten Gebäude"
        },
        {
            "name": "POST /groups",
            "description": "erstellt eine neue Lerngruppe"
        },
        {
            "name": "PUT /groups/{id}",
            "description": "aktualisiert Information einer ausgewählten Lerngruppe"
        },
        {
            "name": "DELETE /groups/{id}",
            "description": "löscht eine Lerngruppe"
        },
        {
            "name": "GET /groups",
            "description": "listet alle Lerngruppen"
        },
        {
            "name": "GET /groups/{id}",
            "description": "zeigt eine ausgewählte Lerngruppe"
        },
        {
            "name": "POST /groups/{id}/members",
            "description": "fügt ein neues Mitglied zur Lerngruppe hinzu"
        },
        {
            "name": "DELETE /groups/{id}/members/{id}",
            "description": "löscht ein Mitglied aus der Lerngruppe"
        },
        {
            "name": "GET /groups/{id}/members",
            "description": "zeigt alle Mitglieder einer ausgewählten Lerngruppe"
        },
        {
            "name": "GET /groups/{id}/members/{id}",
            "description": "zeigt eines ausgewählten Mitglieder einer ausgewählten Lerngruppe"
        },
        {
            "name": "POST /groups/{id}/notes",
            "description": "legt eine neue Notiz in der Lerngruppe an"
        },
        {
            "name": "PUT /groups/{id}/notes/{id}",
            "description": "bearbeitet eine Notiz in der Lerngruppe"
        },
        {
            "name": "DELETE /groups/{id}/notes/{id}",
            "description": "löscht eine Notiz in der Lerngruppe"
        },
        {
            "name": "GET /groups/{id}/notes",
            "description": "listet alle Notizen einer Lerngruppe"
        },
        {
            "name": "GET /groups/{id}/notes/{id}",
            "description": "zeigt Details einer Notizen"
        },
        {
            "name": "GET /groups/{id}/members/{id}/notes",
            "description": "listet Notizen eines Mitgliedes in der ausgewählten Lerngruppe"
        },
        {
            "name": "POST /notes",
            "description": "fügt eine öffentliche Notiz hinzu"
        },
        {
            "name": "GET /notes",
            "description": "listet alle öffentliche Notizen"
        },
        {
            "name": "GET /notes/{id]",
            "description": "zeigt Details einer ausgewählten Notiz"
        },
        {
            "name": "PUT /notes/{id]",
            "description": "aktualisiert Details einer ausgewählten Notiz"
        },
        {
            "name": "DELETE /notes/{id]",
            "description": "löscht eine ausgewählte Notiz"
        },
        {
            "name": "POST /users",
            "description": "fügt einen neuen Benutzer nach Registrierung in die Datenbank"
        },
        {
            "name": "PUT /users/{id}",
            "description": "aktualisiert Information eines Users"
        }
    ];
});