package com.example.restapi.config;

import com.example.restapi.model.Pelicula;
import com.example.restapi.model.Series;
import com.example.restapi.model.Usuario;
import com.example.restapi.model.Generos;
import com.example.restapi.model.Perfil;
import com.example.restapi.repository.PeliculaRepository;
import com.example.restapi.repository.SerieRepository;
import com.example.restapi.repository.UsuarioRepository;
import com.example.restapi.repository.PerfilRepository;
import java.util.Collections;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class inicializarDatos implements CommandLineRunner {

        @Autowired
        private PeliculaRepository peliculaRepository;

        @Autowired
        private SerieRepository serieRepository;
        
        @Autowired
        private UsuarioRepository usuarioRepository;

        @Autowired
        private PerfilRepository perfilRepository;

        @Override
        public void run(String... args) throws Exception {
                // Vaciar las tablas antes de inicializar datos
                usuarioRepository.deleteAll(); 
                perfilRepository.deleteAll();
                peliculaRepository.deleteAll();
                serieRepository.deleteAll();

                // Inicializar usuarios de prueba
                Usuario usu1 = new Usuario(
                        
                        "qw", 
                        "qw",
                        "qw@deustostream.es",
                        "qw"
                );

                Usuario usu2 = new Usuario(
                        
                        "qw", 
                        "qw",
                        "qw@gmail.com",
                        "qw"
                );

                usuarioRepository.save(usu1);
                usuarioRepository.save(usu2);

                // Inicializar películas de prueba
                Pelicula pelicula1 = new Pelicula(
                                "Interstellar",           // Título
                                Generos.CIENCIA_FICCION,  // Género
                                169,                      // Duración en minutos
                                2014,                     // Año
                                "Un grupo de astronautas viaja a través de un agujero de gusano en busca de un nuevo hogar para la humanidad.", // Sinopsis
                                "https://m.media-amazon.com/images/I/61i7admY+hL._AC_UF894,1000_QL80_.jpg" // URL de la imagen
                );
                Pelicula pelicula2 = new Pelicula(
                                "El Padrino",
                                Generos.DRAMA,
                                175,
                                1972,
                                "La historia de la familia mafiosa Corleone y su lucha por el poder en Estados Unidos.",
                                "https://m.media-amazon.com/images/I/41WMFqes1iL._AC_UF894,1000_QL80_.jpg"
                );
                Pelicula pelicula3 = new Pelicula(
                                "Pulp Fiction",
                                Generos.AVENTURA,
                                154,
                                1994,
                                "Historias entrelazadas de crimen y redención en Los Ángeles.",
                                "https://m.media-amazon.com/images/I/81IOViIosKL.jpg"
                );
                Pelicula pelicula4 = new Pelicula(
                                "La La Land",
                                Generos.MUSICAL,
                                128,
                                2016,
                                "Un pianista de jazz y una actriz en ascenso se enamoran mientras persiguen sus sueños en Los Ángeles.",
                                "https://m.media-amazon.com/images/I/91jrKX9xjQL.jpg"
                );
                Pelicula pelicula5 = new Pelicula(
                                "El Señor de los Anillos: La Comunidad del Anillo",
                                Generos.FANTASIA,
                                178,
                                2001,
                                "Un hobbit emprende un peligroso viaje para destruir un anillo mágico y salvar la Tierra Media.",
                                "https://m.media-amazon.com/images/I/6143TqGItiL.jpg"
                );
                Pelicula pelicula6 = new Pelicula(
                                "Forrest Gump",
                                Generos.DRAMA,
                                142,
                                1994,
                                "La vida de un hombre con un coeficiente intelectual bajo que presencia y participa en eventos históricos clave del siglo XX.",
                                "https://m.media-amazon.com/images/I/61oZBKzdPVL.jpg"
                );
                Pelicula pelicula7 = new Pelicula(
                                "El Caballero Oscuro",
                                Generos.ACCION,
                                152,
                                2008,
                                "Batman enfrenta al Joker, un criminal caótico que siembra el caos en Gotham.",
                                "https://m.media-amazon.com/images/I/91aHN3Y8o4L.jpg"
                );
                Pelicula pelicula8 = new Pelicula(
                                "Matrix",
                                Generos.CIENCIA_FICCION,
                                136,
                                1999,
                                "Un hacker descubre la verdadera naturaleza de su realidad y su papel en la guerra contra sus controladores.",
                                "https://static.posters.cz/image/1300/posters/matrix-hacker-i104636.jpg"
                );

                Pelicula pelicula9 = new Pelicula(
                                "Avatar",
                                Generos.AVENTURA,
                                162,
                                2009,
                                "Un ex marine se infiltra en una civilización alienígena para ayudar a su especie.",
                                "https://m.media-amazon.com/images/I/61uspTokDBL._AC_UF894,1000_QL80_.jpg"
                );
                Pelicula pelicula10 = new Pelicula(
                                "Titanic",
                                Generos.ROMANCE,
                                195,
                                1997,
                                "Una joven aristócrata y un artista pobre se enamoran a bordo del infame transatlántico.",
                                "https://m.media-amazon.com/images/I/91zGp74Qc4L.jpg"
                );

                Pelicula pelicula11 = new Pelicula(
                                "Gladiator",
                                Generos.ACCION,
                                155,
                                2000,
                                "Un general romano es traicionado y se convierte en gladiador para vengar la muerte de su familia.",
                                "https://i.ebayimg.com/images/g/dpkAAOSw~llgqj0q/s-l1200.jpg"
                );

                Pelicula pelicula12 = new Pelicula(
                                "Jurassic Park",
                                Generos.AVENTURA,
                                127,
                                1993,
                                "Un parque temático con dinosaurios clonados se convierte en un caos cuando los animales escapan.",
                                "https://i.etsystatic.com/18242346/r/il/1d6c94/4608692380/il_fullxfull.4608692380_ooro.jpg"
                );

                Pelicula pelicula13 = new Pelicula(
                                "Inception",
                                Generos.CIENCIA_FICCION,
                                148,
                                2010,
                                "Un ladrón especializado en robar secretos de los sueños es contratado para implantar una idea en la mente de un objetivo.",
                                "https://www.originalfilmart.com/cdn/shop/files/inception_2010_french_original_film_art_5000x.webp?v=1686692704"
                );

                Pelicula pelicula14 = new Pelicula(
                                "The Shawshank Redemption",
                                Generos.DRAMA,
                                142,
                                1994,
                                "La historia de un banquero condenado a cadena perpetua que encuentra esperanza y amistad en prisión.",
                                "https://m.media-amazon.com/images/I/71VNhykMgNL._AC_UF894,1000_QL80_.jpg"
                );

                Pelicula pelicula15 = new Pelicula(
                                "The Godfather Part II",
                                Generos.DRAMA,
                                202,
                                1974,
                                "La historia de la familia Corleone continúa mientras Michael Corleone se convierte en el nuevo jefe de la mafia.",
                                "https://i.ebayimg.com/00/s/MTYwMFgxMDY2/z/WfUAAOSw44Zj8lry/$_57.JPG?set_id=880000500F"
                );
        
                peliculaRepository.save(pelicula1);
                peliculaRepository.save(pelicula2);
                peliculaRepository.save(pelicula3);
                peliculaRepository.save(pelicula4);
                peliculaRepository.save(pelicula5);
                peliculaRepository.save(pelicula6);
                peliculaRepository.save(pelicula7);
                peliculaRepository.save(pelicula8);
                peliculaRepository.save(pelicula9);
                peliculaRepository.save(pelicula10);
                peliculaRepository.save(pelicula11);
                peliculaRepository.save(pelicula12);
                peliculaRepository.save(pelicula13);
                peliculaRepository.save(pelicula14);
                peliculaRepository.save(pelicula15);
        
                // Inicializar series de prueba
                Series serie1 = new Series(
                                "Game of Thrones",        // Título
                                2011,                     // Año de inicio
                                "Nobles familias luchan por el control del Trono de Hierro en los Siete Reinos de Westeros.", // Descripción
                                Generos.FANTASIA,         // Género
                                Collections.emptyList(),  // Lista de capítulos inicialmente vacía
                                "https://i.ebayimg.com/images/g/QfcAAOSwPmRk1YGD/s-l1200.jpg" // URL de la imagen
                );
                Series serie2 = new Series(
                                "The Office",
                                2005,
                                "Comedia que retrata la vida cotidiana de los empleados de una oficina en Scranton, Pensilvania.",
                                Generos.COMEDIA,
                                Collections.emptyList(),
                                "https://ih1.redbubble.net/image.2138428860.4920/flat,750x,075,f-pad,750x1000,f8f8f8.jpg"
                );
                Series serie3 = new Series(
                                "Friends",
                                1994,
                                "Seis amigos navegan por las complejidades de la vida y el amor en Nueva York.",
                                Generos.COMEDIA,
                                Collections.emptyList(),
                                "https://img.posterstore.com/zoom/wb0193-8friends-thefriendsportrayedno150x70.jpg"
                );
                Series serie4 = new Series(
                                "The Crown",
                                2016,
                                "Crónica de la vida de la Reina Isabel II y los eventos políticos que moldearon su reinado.",
                                Generos.DRAMA,
                                Collections.emptyList(),
                                "https://pics.filmaffinity.com/the_crown-838357032-large.jpg"
                );
                Series serie5 = new Series(
                                "Stranger Things",
                                2016,
                                "Un grupo de niños descubre misterios sobrenaturales en su pequeño pueblo.",
                                Generos.CIENCIA_FICCION,
                                Collections.emptyList(),
                                "https://www.yourdecoration.es/cdn/shop/products/pyramid-pp34422-stranger-things-one-sheet-season-2-poster-61x91-5cm.jpg?v=1709734663"
                );
                Series serie6 = new Series(
                                "The Simpsons",
                                1989,
                                "Las aventuras y desventuras de una familia amarilla en la ciudad de Springfield.",
                                Generos.ANIMACION,
                                Collections.emptyList(),
                                "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1491519331i/28549904.jpg"
                );

                serieRepository.save(serie1);
                serieRepository.save(serie2);
                serieRepository.save(serie3);
                serieRepository.save(serie4);
                serieRepository.save(serie5);
                serieRepository.save(serie6);
        serieRepository.save(serie6);

        // Inicializar usuarios de prueba
        Usuario usuario1 = new Usuario("Esperanza", "Gracia", "prueba@prueba.com","1234" );
        Usuario usuario2 = new Usuario ("nombre", "apellido", "prueba@deustostream.es","1234" );
        usuario1.getPerfiles().get(0).setListaMeGustaPeliculas(Collections.singletonList(pelicula1));
        usuario1.getPerfiles().get(0).setAvatar("perfil4");
        usuario1.getPerfiles().get(0).setEdad(24);

        Perfil perfil1 = new Perfil("Hermano", "perfil1");
        perfil1.setUsuario(usuario1);
        perfil1.setEdad(12);
        usuario1.getPerfiles().add(perfil1);

        Perfil perfil2 = new Perfil("Ricardo Palomares", "perfil3");
        perfil2.setUsuario(usuario1);
        perfil2.setEdad(50);
        usuario1.getPerfiles().add(perfil2);



        usuarioRepository.save(usuario1);
        usuarioRepository.save(usuario2);

        // Asignar películas y series a los usuarios
        

        }
}