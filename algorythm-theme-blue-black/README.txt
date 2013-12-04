Due to a bug the language portlet has not enough permissions
so that users/guests cannot change the language but administrator only
(see https://issues.liferay.com/browse/LPS-30356).
To solve this issue go to
control panel->roles->{User,Guest}->Define Permissions->Language
and make sure the View-permission is added to portal scope. 

To remove raster image scanline exception at deployment delete
ROOT/html/themes/_unstyled/images/calendar/calendar_drop_shadow.png
ROOT/html/themes/classic/images/calendar/calendar_drop_shadow.png
ROOT/html/themes/control_panel/images/calendar/calendar_drop_shadow.png
welcome-theme/images/calendar/calendar_drop_shadow.png