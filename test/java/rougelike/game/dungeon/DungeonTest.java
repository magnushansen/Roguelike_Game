package rougelike.game.dungeon;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

@DisplayName("Dungeon Tests")
class DungeonTest {
    
    private Dungeon dungeon;
    private char[][][] testLayout;
    private static final String TEST_NAME = "Test Dungeon";
    
    @BeforeEach
    void setUp() {
        testLayout = createTestLayout();
        dungeon = new Dungeon(TEST_NAME, testLayout);
    }
    
    private char[][][] createTestLayout() {
        return new char[][][] {
            {
                {'W', 'W', 'W', 'W', 'W'},
                {'W', ' ', ' ', ' ', 'W'},
                {'W', ' ', 'E', ' ', 'W'},
                {'W', ' ', ' ', ' ', 'W'},
                {'W', 'W', 'W', 'W', 'W'}
            },
            {
                {'W', 'W', 'W', 'W', 'W'},
                {'W', ' ', ' ', ' ', 'W'},
                {'W', ' ', ' ', ' ', 'W'},
                {'W', ' ', ' ', ' ', 'W'},
                {'W', 'W', 'W', 'W', 'W'}
            }
        };
    }
    
    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
        
        @Test
        @DisplayName("Should create dungeon with valid parameters")
        void shouldCreateDungeonWithValidParameters() {
            assertNotNull(dungeon);
            assertEquals(TEST_NAME, dungeon.getName());
            assertArrayEquals(testLayout, dungeon.getLayout());
        }
        
        @Test
        @DisplayName("Should handle null name")
        void shouldHandleNullName() {
            Dungeon nullNameDungeon = new Dungeon(null, testLayout);
            assertNull(nullNameDungeon.getName());
            assertArrayEquals(testLayout, nullNameDungeon.getLayout());
        }
        
        @Test
        @DisplayName("Should handle empty name")
        void shouldHandleEmptyName() {
            Dungeon emptyNameDungeon = new Dungeon("", testLayout);
            assertEquals("", emptyNameDungeon.getName());
            assertArrayEquals(testLayout, emptyNameDungeon.getLayout());
        }
        
        @Test
        @DisplayName("Should handle null layout")
        void shouldHandleNullLayout() {
            Dungeon nullLayoutDungeon = new Dungeon(TEST_NAME, null);
            assertEquals(TEST_NAME, nullLayoutDungeon.getName());
            assertNull(nullLayoutDungeon.getLayout());
        }
        
        @Test
        @DisplayName("Should handle empty layout")
        void shouldHandleEmptyLayout() {
            char[][][] emptyLayout = new char[0][][];
            Dungeon emptyLayoutDungeon = new Dungeon(TEST_NAME, emptyLayout);
            assertEquals(TEST_NAME, emptyLayoutDungeon.getName());
            assertEquals(0, emptyLayoutDungeon.getLayout().length);
        }
    }
    
    @Nested
    @DisplayName("Getter Tests")
    class GetterTests {
        
        @Test
        @DisplayName("Should return correct name")
        void shouldReturnCorrectName() {
            assertEquals(TEST_NAME, dungeon.getName());
        }
        
        @Test
        @DisplayName("Should return correct layout")
        void shouldReturnCorrectLayout() {
            char[][][] layout = dungeon.getLayout();
            assertNotNull(layout);
            assertEquals(2, layout.length);
            assertEquals(5, layout[0].length);
            assertEquals(5, layout[0][0].length);
        }
        
        @Test
        @DisplayName("Should return reference to original layout")
        void shouldReturnReferenceToOriginalLayout() {
            char[][][] layout = dungeon.getLayout();
            assertSame(testLayout, layout);
        }
        
        @Test
        @DisplayName("Should allow layout modification through reference")
        void shouldAllowLayoutModificationThroughReference() {
            char[][][] layout = dungeon.getLayout();
            char originalValue = layout[0][1][1];
            layout[0][1][1] = 'X';
            
            assertEquals('X', dungeon.getLayout()[0][1][1]);
            assertNotEquals(originalValue, dungeon.getLayout()[0][1][1]);
        }
    }
    
    @Nested
    @DisplayName("Layout Structure Tests")
    class LayoutStructureTests {
        
        @Test
        @DisplayName("Should handle single level dungeons")
        void shouldHandleSingleLevelDungeons() {
            char[][][] singleLevel = { testLayout[0] };
            Dungeon singleLevelDungeon = new Dungeon("Single Level", singleLevel);
            
            assertEquals(1, singleLevelDungeon.getLayout().length);
        }
        
        @Test
        @DisplayName("Should handle multi-level dungeons")
        void shouldHandleMultiLevelDungeons() {
            assertEquals(2, dungeon.getLayout().length);
        }
        
        @Test
        @DisplayName("Should handle irregular layouts")
        void shouldHandleIrregularLayouts() {
            char[][][] irregularLayout = {
                {
                    {'W', 'W', 'W'},
                    {'W', ' '}
                },
                {
                    {'W'},
                    {'W', ' ', 'W', 'W'}
                }
            };
            
            Dungeon irregularDungeon = new Dungeon("Irregular", irregularLayout);
            assertNotNull(irregularDungeon.getLayout());
        }
        
        @Test
        @DisplayName("Should handle moderately sized dungeons")
        void shouldHandleModeratelySizedDungeons() {
            char[][][] mediumDungeon = new char[3][10][10];
            for (int level = 0; level < 3; level++) {
                for (int row = 0; row < 10; row++) {
                    for (int col = 0; col < 10; col++) {
                        mediumDungeon[level][row][col] = ' ';
                    }
                }
            }
            
            Dungeon medium = new Dungeon("Medium Dungeon", mediumDungeon);
            assertEquals(3, medium.getLayout().length);
            assertEquals(10, medium.getLayout()[0].length);
            assertEquals(10, medium.getLayout()[0][0].length);
        }
    }
    
    
    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {
        
        @Test
        @DisplayName("Should generate non-null string representation")
        void shouldGenerateNonNullStringRepresentation() {
            String dungeonString = dungeon.toString();
            assertNotNull(dungeonString);
            assertFalse(dungeonString.isEmpty());
        }
        
        @Test
        @DisplayName("Should include dungeon name in string representation")
        void shouldIncludeDungeonNameInStringRepresentation() {
            String dungeonString = dungeon.toString();
            assertTrue(dungeonString.contains(TEST_NAME));
        }
        
        @Test
        @DisplayName("Should include layout information in string representation")
        void shouldIncludeLayoutInformationInStringRepresentation() {
            String dungeonString = dungeon.toString();
            assertTrue(dungeonString.contains("layout"));
        }
        
        @Test
        @DisplayName("Should handle null name in toString")
        void shouldHandleNullNameInToString() {
            Dungeon nullNameDungeon = new Dungeon(null, testLayout);
            String dungeonString = nullNameDungeon.toString();
            assertNotNull(dungeonString);
        }
        
        @Test
        @DisplayName("Should handle null layout in toString")
        void shouldHandleNullLayoutInToString() {
            Dungeon nullLayoutDungeon = new Dungeon(TEST_NAME, null);
            assertDoesNotThrow(() -> {
                String dungeonString = nullLayoutDungeon.toString();
                assertNotNull(dungeonString);
            });
        }
    }
    
    
    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {
        
        @Test
        @DisplayName("Should handle special characters in name")
        void shouldHandleSpecialCharactersInName() {
            String specialName = "Dungeon with symbols !@#$%";
            Dungeon specialDungeon = new Dungeon(specialName, testLayout);
            assertEquals(specialName, specialDungeon.getName());
        }
    }
}