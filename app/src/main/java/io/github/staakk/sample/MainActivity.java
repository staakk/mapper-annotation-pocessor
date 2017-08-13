package io.github.staakk.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.github.staakk.map.Exclude;
import io.github.staakk.map.Map;
import io.github.staakk.mapper.AToBMapper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        B b = AToBMapper.map(new A());
    }

    public static class B {
        public int foo;

        public int bar;

        private int baz;

        public int getBaz() {
            return baz;
        }

        public void setBaz(int baz) {
            this.baz = baz;
        }
    }

    @Map(B.class)
    public static class A {
        public int foo;

        @Exclude
        public int bar;

        private int baz;

        public int getBaz() {
            return baz;
        }

        public void setBaz(int baz) {
            this.baz = baz;
        }
    }
}
