Due to a bug the language portlet has not enough permissions
so that users/guests cannot change the language but administrator only
(see https://issues.liferay.com/browse/LPS-30356).
To solve this issue go to
control panel->roles->{User,Guest}->Define Permissions->Language
and make sure the View-permission is added to portal scope.
