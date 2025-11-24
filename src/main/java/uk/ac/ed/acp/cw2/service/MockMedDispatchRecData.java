package uk.ac.ed.acp.cw2.service;

import org.springframework.stereotype.Service;
import uk.ac.ed.acp.cw2.data.LngLat;
import uk.ac.ed.acp.cw2.dto.MedDispatchRecDTO;
import uk.ac.ed.acp.cw2.dto.RequirementsDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MockMedDispatchRecData {

    public List<MedDispatchRecDTO> getMockMedDispatchRecAT() {
        List<MedDispatchRecDTO> appletonTowerDispatch = new ArrayList<>() {{
            add(new MedDispatchRecDTO() {{
                id = 1;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 12.3;
                    cooling = true;
                    heating = false;
                    maxCost = 22.4;
                }};
                delivery = new LngLat() {{ lng = -3.1862070567199225; lat = 55.94597836472275; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 2;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 4.8;
                    cooling = false;
                    heating = true;
                    maxCost = 18.9;
                }};
                delivery = new LngLat() {{ lng = -3.1840505360201234; lat = 55.946109624957415; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 3;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 7.2;
                    cooling = true;
                    heating = false;
                    maxCost = 26.3;
                }};
                delivery = new LngLat() {{ lng = -3.1858004013780317; lat = 55.94337610177331; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 4;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 9.4;
                    cooling = false;
                    heating = false;
                    maxCost = 19.7;
                }};
                delivery = new LngLat() {{ lng = -3.1836427953562065; lat = 55.94498714187296; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 5;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 13.8;
                    cooling = true;
                    heating = false;
                    maxCost = 24.2;
                }};
                delivery = new LngLat() {{ lng = -3.1884897900130795; lat = 55.94507989445435; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 6;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 8.1;
                    cooling = false;
                    heating = true;
                    maxCost = 20.5;
                }};
                delivery = new LngLat() {{ lng = -3.182759821338891; lat = 55.94970532254246; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 7;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 6.7;
                    cooling = true;
                    heating = false;
                    maxCost = 27.1;
                }};
                delivery = new LngLat() {{ lng = -3.1721401423491216; lat = 55.94474168517581; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 8;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 11.9;
                    cooling = false;
                    heating = false;
                    maxCost = 21.3;
                }};
                delivery = new LngLat() {{ lng = -3.1754371128160983; lat = 55.940854417078725; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 9;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 10.4;
                    cooling = true;
                    heating = true;
                    maxCost = 25.8;
                }};
                delivery = new LngLat() {{ lng = -3.180117931258195; lat = 55.94181042756398; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 10;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 14.6;
                    cooling = false;
                    heating = true;
                    maxCost = 23.4;
                }};
                delivery = new LngLat() {{ lng = -3.1954834104984684; lat = 55.944524653000116; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 11;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 5.3;
                    cooling = true;
                    heating = false;
                    maxCost = 17.9;
                }};
                delivery = new LngLat() {{ lng = -3.1924549283904184; lat = 55.94862613270408; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 12;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 9.2;
                    cooling = false;
                    heating = false;
                    maxCost = 28.6;
                }};
                delivery = new LngLat() {{ lng = -3.1856227001897253; lat = 55.95214168782093; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 13;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 7.1;
                    cooling = true;
                    heating = false;
                    maxCost = 24.9;
                }};
                delivery = new LngLat() {{ lng = -3.1760919115071147; lat = 55.94939687865167; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 14;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 8.8;
                    cooling = false;
                    heating = true;
                    maxCost = 19.5;
                }};
                delivery = new LngLat() {{ lng = -3.182981644436893; lat = 55.94230387973528; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 15;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 6.4;
                    cooling = true;
                    heating = false;
                    maxCost = 16.8;
                }};
                delivery = new LngLat() {{ lng = -3.1919034109323547; lat = 55.94301322016224; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 16;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 12.0;
                    cooling = false;
                    heating = false;
                    maxCost = 29.0;
                }};
                delivery = new LngLat() {{ lng = -3.1820455037837974; lat = 55.94060756087748; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 17;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 5.9;
                    cooling = true;
                    heating = true;
                    maxCost = 20.7;
                }};
                delivery = new LngLat() {{ lng = -3.1843841138101823; lat = 55.94542780635544; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 18;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 14.1;
                    cooling = false;
                    heating = false;
                    maxCost = 26.1;
                }};
                delivery = new LngLat() {{ lng = -3.1821200220333026; lat = 55.94341797515054; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 19;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 9.9;
                    cooling = true;
                    heating = false;
                    maxCost = 17.5;
                }};
                delivery = new LngLat() {{ lng = -3.182522527237751; lat = 55.94472343813234; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 20;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 11.6;
                    cooling = false;
                    heating = true;
                    maxCost = 22.8;
                }};
                delivery = new LngLat() {{ lng = -3.1822374193846485; lat = 55.9461039877973; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 21;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 4.3;
                    cooling = true;
                    heating = false;
                    maxCost = 15.6;
                }};
                delivery = new LngLat() {{ lng = -3.19848061290557; lat = 55.943313274596306; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 22;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 13.5;
                    cooling = false;
                    heating = true;
                    maxCost = 30.0;
                }};
                delivery = new LngLat() {{ lng = -3.189815941113096; lat = 55.94439772734589; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 23;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 10.7;
                    cooling = true;
                    heating = false;
                    maxCost = 19.3;
                }};
                delivery = new LngLat() {{ lng = -3.192250740012213; lat = 55.94636153957521; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 24;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 15.2;
                    cooling = false;
                    heating = false;
                    maxCost = 27.3;
                }};
                delivery = new LngLat() {{ lng = -3.177609201845627; lat = 55.946093060311426; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 25;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 12.8;
                    cooling = true;
                    heating = true;
                    maxCost = 23.9;
                }};
                delivery = new LngLat() {{ lng = -3.166913025653969; lat = 55.95491849552985; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 26;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 3.7;
                    cooling = false;
                    heating = false;
                    maxCost = 18.2;
                }};
                delivery = new LngLat() {{ lng = -3.1917198024173956; lat = 55.938717830387674; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 27;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 16.0;
                    cooling = true;
                    heating = false;
                    maxCost = 24.4;
                }};
                delivery = new LngLat() {{ lng = -3.1782560699893168; lat = 55.93743313391249; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 28;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 6.0;
                    cooling = false;
                    heating = true;
                    maxCost = 21.8;
                }};
                delivery = new LngLat() {{ lng = -3.1848036338141696; lat = 55.94419452418333; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 29;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 14.4;
                    cooling = true;
                    heating = false;
                    maxCost = 28.0;
                }};
                delivery = new LngLat() {{ lng = -3.183509883690732; lat = 55.944621545134055; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 30;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 9.0;
                    cooling = false;
                    heating = false;
                    maxCost = 17.2;
                }};
                delivery = new LngLat() {{ lng = -3.1700825213927715; lat = 55.93962743351517; }};
            }});

        }};
        return appletonTowerDispatch;
    }

    // princes street dispatches
    public List<MedDispatchRecDTO> getMockMedDispatchRecPT() {
        List<MedDispatchRecDTO> princesStreetDispatches = new ArrayList<>() {{
            add(new MedDispatchRecDTO() {{
                id = 32;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 10.5;
                    cooling = true;
                    heating = false;
                    maxCost = 23.0;
                }};
                delivery = new LngLat() {{ lng = -3.190388477146314; lat = 55.95165627566874; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 33;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 12.0;
                    cooling = false;
                    heating = true;
                    maxCost = 25.0;
                }};
                delivery = new LngLat() {{ lng = -3.2035138060492727; lat = 55.953905833603244; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 34;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 8.7;
                    cooling = true;
                    heating = false;
                    maxCost = 19.8;
                }};
                delivery = new LngLat() {{ lng = -3.19637076991188; lat = 55.95688004842492; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 35;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 11.2;
                    cooling = false;
                    heating = false;
                    maxCost = 21.5;
                }};
                delivery = new LngLat() {{ lng = -3.19904940846331; lat = 55.9483816855209; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 36;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 9.6;
                    cooling = true;
                    heating = false;
                    maxCost = 22.7;
                }};
                delivery = new LngLat() {{ lng = -3.210701486163515; lat = 55.95195622427869; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 37;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 7.8;
                    cooling = false;
                    heating = true;
                    maxCost = 20.1;
                }};
                delivery = new LngLat() {{ lng = -3.1827947982832825; lat = 55.94994999622338; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 38;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 13.1;
                    cooling = true;
                    heating = false;
                    maxCost = 26.3;
                }};
                delivery = new LngLat() {{ lng = -3.1976356188310433; lat = 55.9550217810737; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 39;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 10.0;
                    cooling = false;
                    heating = false;
                    maxCost = 24.6;
                }};
                delivery = new LngLat() {{ lng = -3.201973435432592; lat = 55.95167913086499; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 40;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 11.8;
                    cooling = true;
                    heating = true;
                    maxCost = 27.4;
                }};
                delivery = new LngLat() {{ lng = -3.209399972364139; lat = 55.95923381688414; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 41;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 8.9;
                    cooling = false;
                    heating = true;
                    maxCost = 22.5;
                }};
                delivery = new LngLat() {{ lng = -3.2026456134952355; lat = 55.95745055602825; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 42;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 12.5;
                    cooling = true;
                    heating = false;
                    maxCost = 28.0;
                }};
                delivery = new LngLat() {{ lng = -3.1875096134511125; lat = 55.95707224665779; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 43;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 9.3;
                    cooling = false;
                    heating = true;
                    maxCost = 19.7;
                }};
                delivery = new LngLat() {{ lng = -3.1897345070863707; lat = 55.95556136176114; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 44;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 14.0;
                    cooling = true;
                    heating = false;
                    maxCost = 25.9;
                }};
                delivery = new LngLat() {{ lng = -3.1993972421149124; lat = 55.954305985676825; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 45;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 13.6;
                    cooling = false;
                    heating = false;
                    maxCost = 26.7;
                }};
                delivery = new LngLat() {{ lng = -3.21261617804754; lat = 55.95628644040852; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 46;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 7.5;
                    cooling = true;
                    heating = false;
                    maxCost = 21.2;
                }};
                delivery = new LngLat() {{ lng = -3.1940609298869163; lat = 55.949577652939354; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 47;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 8.8;
                    cooling = false;
                    heating = true;
                    maxCost = 22.9;
                }};
                delivery = new LngLat() {{ lng = -3.1877192268284773; lat = 55.954403215672414; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 48;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 11.1;
                    cooling = true;
                    heating = false;
                    maxCost = 24.5;
                }};
                delivery = new LngLat() {{ lng = -3.193173050506431; lat = 55.953206447059955; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 49;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 9.7;
                    cooling = false;
                    heating = true;
                    maxCost = 23.3;
                }};
                delivery = new LngLat() {{ lng = -3.200567093842608; lat = 55.95579877703952; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 50;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 10.2;
                    cooling = true;
                    heating = false;
                    maxCost = 22.8;
                }};
                delivery = new LngLat() {{ lng = -3.2011251076623637; lat = 55.956867802056024; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 51;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 8.6;
                    cooling = false;
                    heating = false;
                    maxCost = 20.0;
                }};
                delivery = new LngLat() {{ lng = -3.2009963283990714; lat = 55.95355352837598; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 52;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 12.4;
                    cooling = true;
                    heating = true;
                    maxCost = 26.1;
                }};
                delivery = new LngLat() {{ lng = -3.2026662929102088; lat = 55.95610764584046; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 53;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 9.9;
                    cooling = false;
                    heating = false;
                    maxCost = 23.7;
                }};
                delivery = new LngLat() {{ lng = -3.1997736287824807; lat = 55.95729529125728; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 54;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 7.3;
                    cooling = true;
                    heating = false;
                    maxCost = 21.0;
                }};
                delivery = new LngLat() {{ lng = -3.2000210571448235; lat = 55.95023890040355; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 55;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 8.5;
                    cooling = false;
                    heating = true;
                    maxCost = 22.2;
                }};
                delivery = new LngLat() {{ lng = -3.1956662024029185; lat = 55.949720463161356; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 56;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 10.9;
                    cooling = true;
                    heating = false;
                    maxCost = 24.8;
                }};
                delivery = new LngLat() {{ lng = -3.189741619593633; lat = 55.953360875370294; }};
            }});
            add(new MedDispatchRecDTO() {{
                id = 57;
                date = LocalDate.of(2025, 11, 23);
                time = LocalTime.of(14, 30);
                requirements = new RequirementsDTO() {{
                    capacity = 11.5;
                    cooling = false;
                    heating = true;
                    maxCost = 25.5;
                }};
                delivery = new LngLat() {{ lng = -3.1918862073159744; lat = 55.958207070840075; }};
            }});
        }};
        return princesStreetDispatches;
    }
}
